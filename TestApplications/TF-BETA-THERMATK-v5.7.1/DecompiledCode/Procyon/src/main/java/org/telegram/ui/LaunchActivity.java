// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.content.SharedPreferences$Editor;
import android.content.DialogInterface$OnDismissListener;
import org.telegram.messenger.camera.CameraController;
import org.telegram.messenger.ImageLoader;
import android.view.ViewTreeObserver;
import android.os.Build;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import android.view.View$OnTouchListener;
import android.graphics.drawable.Drawable;
import android.graphics.Shader$TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.View$MeasureSpec;
import android.widget.RelativeLayout;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.PipRoundVideoView;
import android.content.res.Configuration;
import org.telegram.ui.Components.ThemeEditorView;
import android.view.Menu;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.Cells.DrawerAddCell;
import android.view.MotionEvent;
import android.content.pm.PackageInfo;
import org.telegram.ui.Components.UpdateAppAlertDialog;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.messenger.ChatObject;
import android.widget.Toast;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.LocationController;
import android.content.SharedPreferences;
import android.os.StatFs;
import org.telegram.messenger.FileLoader;
import android.view.KeyEvent;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import java.io.File;
import org.telegram.ui.Cells.DrawerUserCell;
import android.app.ActivityManager$TaskDescription;
import org.telegram.messenger.MediaController;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.LinearLayout;
import android.content.DialogInterface$OnCancelListener;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.os.Bundle;
import android.app.Dialog;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import android.content.DialogInterface$OnClickListener;
import android.content.Context;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.ApplicationLoader;
import android.graphics.Point;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import java.util.Iterator;
import java.util.Map;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import android.content.DialogInterface;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import android.os.Build$VERSION;
import org.telegram.messenger.UserConfig;
import android.view.ActionMode;
import org.telegram.ui.Components.TermsOfServiceView;
import org.telegram.ui.Components.RecyclerListView;
import android.widget.FrameLayout;
import android.location.Location;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.ui.Components.PasscodeView;
import android.content.Intent;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import java.util.regex.Pattern;
import org.telegram.ui.ActionBar.AlertDialog;
import java.util.HashMap;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import android.net.Uri;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BlockingUpdateView;
import android.view.View;
import org.telegram.ui.ActionBar.BaseFragment;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.ActionBarLayout;
import android.app.Activity;

public class LaunchActivity extends Activity implements ActionBarLayoutDelegate, NotificationCenterDelegate, DialogsActivityDelegate
{
    private static ArrayList<BaseFragment> layerFragmentsStack;
    private static ArrayList<BaseFragment> mainFragmentsStack;
    private static ArrayList<BaseFragment> rightFragmentsStack;
    private ActionBarLayout actionBarLayout;
    private View backgroundTablet;
    private BlockingUpdateView blockingUpdateView;
    private ArrayList<TLRPC.User> contactsToSend;
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
    private final Pattern locationRegex;
    private Runnable lockRunnable;
    private ViewTreeObserver$OnGlobalLayoutListener onGlobalLayoutListener;
    private Intent passcodeSaveIntent;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private PasscodeView passcodeView;
    private ArrayList<SendMessagesHelper.SendingMediaInfo> photoPathsArray;
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
    
    static {
        LaunchActivity.mainFragmentsStack = new ArrayList<BaseFragment>();
        LaunchActivity.layerFragmentsStack = new ArrayList<BaseFragment>();
        LaunchActivity.rightFragmentsStack = new ArrayList<BaseFragment>();
    }
    
    public LaunchActivity() {
        this.locationRegex = Pattern.compile("geo: ?(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)(,|\\?z=)(-?\\d+)");
    }
    
    private void checkCurrentAccount() {
        final int currentAccount = this.currentAccount;
        if (currentAccount != UserConfig.selectedAccount) {
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
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
        this.updateCurrentConnectionState(this.currentAccount);
    }
    
    private void checkFreeDiscSpace() {
        if (Build$VERSION.SDK_INT >= 26) {
            return;
        }
        Utilities.globalQueue.postRunnable(new _$$Lambda$LaunchActivity$OhtB5MFVTEjvucCdsB03AKPPH2Y(this), 2000L);
    }
    
    private void checkLayout() {
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout != null) {
                final boolean isInMultiwindow = AndroidUtilities.isInMultiwindow;
                int visibility = 0;
                final int n = 0;
                if (!isInMultiwindow && (!AndroidUtilities.isSmallTablet() || this.getResources().getConfiguration().orientation == 2)) {
                    this.tabletFullSize = false;
                    if (this.actionBarLayout.fragmentsStack.size() >= 2) {
                        while (1 < this.actionBarLayout.fragmentsStack.size()) {
                            final BaseFragment e = this.actionBarLayout.fragmentsStack.get(1);
                            if (e instanceof ChatActivity) {
                                ((ChatActivity)e).setIgnoreAttachOnPause(true);
                            }
                            e.onPause();
                            this.actionBarLayout.fragmentsStack.remove(1);
                            this.rightActionBarLayout.fragmentsStack.add(e);
                        }
                        if (this.passcodeView.getVisibility() != 0) {
                            this.actionBarLayout.showLastFragment();
                            this.rightActionBarLayout.showLastFragment();
                        }
                    }
                    final ActionBarLayout rightActionBarLayout = this.rightActionBarLayout;
                    int visibility2;
                    if (rightActionBarLayout.fragmentsStack.isEmpty()) {
                        visibility2 = 8;
                    }
                    else {
                        visibility2 = 0;
                    }
                    rightActionBarLayout.setVisibility(visibility2);
                    final View backgroundTablet = this.backgroundTablet;
                    int visibility3;
                    if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        visibility3 = 0;
                    }
                    else {
                        visibility3 = 8;
                    }
                    backgroundTablet.setVisibility(visibility3);
                    final FrameLayout shadowTabletSide = this.shadowTabletSide;
                    int visibility4;
                    if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                        visibility4 = n;
                    }
                    else {
                        visibility4 = 8;
                    }
                    shadowTabletSide.setVisibility(visibility4);
                }
                else {
                    this.tabletFullSize = true;
                    if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.rightActionBarLayout.fragmentsStack.size() > 0) {
                            final BaseFragment e2 = this.rightActionBarLayout.fragmentsStack.get(0);
                            if (e2 instanceof ChatActivity) {
                                ((ChatActivity)e2).setIgnoreAttachOnPause(true);
                            }
                            e2.onPause();
                            this.rightActionBarLayout.fragmentsStack.remove(0);
                            this.actionBarLayout.fragmentsStack.add(e2);
                        }
                        if (this.passcodeView.getVisibility() != 0) {
                            this.actionBarLayout.showLastFragment();
                        }
                    }
                    this.shadowTabletSide.setVisibility(8);
                    this.rightActionBarLayout.setVisibility(8);
                    final View backgroundTablet2 = this.backgroundTablet;
                    if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                        visibility = 8;
                    }
                    backgroundTablet2.setVisibility(visibility);
                }
            }
        }
    }
    
    private String getStringForLanguageAlert(final HashMap<String, String> hashMap, final String key, final int n) {
        String string;
        if ((string = hashMap.get(key)) == null) {
            string = LocaleController.getString(key, n);
        }
        return string;
    }
    
    private boolean handleIntent(final Intent p0, final boolean p1, final boolean p2, final boolean p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: invokestatic    org/telegram/messenger/AndroidUtilities.handleProxyIntent:(Landroid/app/Activity;Landroid/content/Intent;)Z
        //     5: ifeq            37
        //     8: aload_0        
        //     9: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //    12: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //    15: invokestatic    org/telegram/messenger/AndroidUtilities.isTablet:()Z
        //    18: ifeq            35
        //    21: aload_0        
        //    22: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //    25: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //    28: aload_0        
        //    29: getfield        org/telegram/ui/LaunchActivity.rightActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //    32: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //    35: iconst_1       
        //    36: ireturn        
        //    37: invokestatic    org/telegram/ui/PhotoViewer.hasInstance:()Z
        //    40: ifeq            77
        //    43: invokestatic    org/telegram/ui/PhotoViewer.getInstance:()Lorg/telegram/ui/PhotoViewer;
        //    46: invokevirtual   org/telegram/ui/PhotoViewer.isVisible:()Z
        //    49: ifeq            77
        //    52: aload_1        
        //    53: ifnull          69
        //    56: ldc_w           "android.intent.action.MAIN"
        //    59: aload_1        
        //    60: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //    63: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    66: ifne            77
        //    69: invokestatic    org/telegram/ui/PhotoViewer.getInstance:()Lorg/telegram/ui/PhotoViewer;
        //    72: iconst_0       
        //    73: iconst_1       
        //    74: invokevirtual   org/telegram/ui/PhotoViewer.closePhoto:(ZZ)V
        //    77: aload_1        
        //    78: invokevirtual   android/content/Intent.getFlags:()I
        //    81: istore          5
        //    83: iconst_1       
        //    84: newarray        I
        //    86: astore          6
        //    88: aload           6
        //    90: iconst_0       
        //    91: aload_1        
        //    92: ldc_w           "currentAccount"
        //    95: getstatic       org/telegram/messenger/UserConfig.selectedAccount:I
        //    98: invokevirtual   android/content/Intent.getIntExtra:(Ljava/lang/String;I)I
        //   101: iastore        
        //   102: aload_0        
        //   103: aload           6
        //   105: iconst_0       
        //   106: iaload         
        //   107: iconst_1       
        //   108: invokevirtual   org/telegram/ui/LaunchActivity.switchToAccount:(IZ)V
        //   111: iload           4
        //   113: ifne            161
        //   116: iconst_1       
        //   117: invokestatic    org/telegram/messenger/AndroidUtilities.needShowPasscode:(Z)Z
        //   120: ifne            129
        //   123: getstatic       org/telegram/messenger/SharedConfig.isWaitingForPasscodeEnter:Z
        //   126: ifeq            161
        //   129: aload_0        
        //   130: invokespecial   org/telegram/ui/LaunchActivity.showPasscodeActivity:()V
        //   133: aload_0        
        //   134: aload_1        
        //   135: putfield        org/telegram/ui/LaunchActivity.passcodeSaveIntent:Landroid/content/Intent;
        //   138: aload_0        
        //   139: iload_2        
        //   140: putfield        org/telegram/ui/LaunchActivity.passcodeSaveIntentIsNew:Z
        //   143: aload_0        
        //   144: iload_3        
        //   145: putfield        org/telegram/ui/LaunchActivity.passcodeSaveIntentIsRestore:Z
        //   148: aload_0        
        //   149: getfield        org/telegram/ui/LaunchActivity.currentAccount:I
        //   152: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //   155: iconst_0       
        //   156: invokevirtual   org/telegram/messenger/UserConfig.saveConfig:(Z)V
        //   159: iconst_0       
        //   160: ireturn        
        //   161: getstatic       org/telegram/messenger/SharedConfig.directShare:Z
        //   164: ifeq            215
        //   167: aload_1        
        //   168: ifnull          215
        //   171: aload_1        
        //   172: invokevirtual   android/content/Intent.getExtras:()Landroid/os/Bundle;
        //   175: ifnull          215
        //   178: aload_1        
        //   179: invokevirtual   android/content/Intent.getExtras:()Landroid/os/Bundle;
        //   182: ldc_w           "dialogId"
        //   185: lconst_0       
        //   186: invokevirtual   android/os/Bundle.getLong:(Ljava/lang/String;J)J
        //   189: lstore          7
        //   191: aload_1        
        //   192: invokevirtual   android/content/Intent.getExtras:()Landroid/os/Bundle;
        //   195: ldc_w           "hash"
        //   198: lconst_0       
        //   199: invokevirtual   android/os/Bundle.getLong:(Ljava/lang/String;J)J
        //   202: getstatic       org/telegram/messenger/SharedConfig.directShareHash:J
        //   205: lcmp           
        //   206: ifeq            212
        //   209: goto            215
        //   212: goto            218
        //   215: lconst_0       
        //   216: lstore          7
        //   218: aload_0        
        //   219: aconst_null    
        //   220: putfield        org/telegram/ui/LaunchActivity.photoPathsArray:Ljava/util/ArrayList;
        //   223: aload_0        
        //   224: aconst_null    
        //   225: putfield        org/telegram/ui/LaunchActivity.videoPath:Ljava/lang/String;
        //   228: aload_0        
        //   229: aconst_null    
        //   230: putfield        org/telegram/ui/LaunchActivity.sendingText:Ljava/lang/String;
        //   233: aload_0        
        //   234: aconst_null    
        //   235: putfield        org/telegram/ui/LaunchActivity.sendingLocation:Landroid/location/Location;
        //   238: aload_0        
        //   239: aconst_null    
        //   240: putfield        org/telegram/ui/LaunchActivity.documentsPathsArray:Ljava/util/ArrayList;
        //   243: aload_0        
        //   244: aconst_null    
        //   245: putfield        org/telegram/ui/LaunchActivity.documentsOriginalPathsArray:Ljava/util/ArrayList;
        //   248: aload_0        
        //   249: aconst_null    
        //   250: putfield        org/telegram/ui/LaunchActivity.documentsMimeType:Ljava/lang/String;
        //   253: aload_0        
        //   254: aconst_null    
        //   255: putfield        org/telegram/ui/LaunchActivity.documentsUrisArray:Ljava/util/ArrayList;
        //   258: aload_0        
        //   259: aconst_null    
        //   260: putfield        org/telegram/ui/LaunchActivity.contactsToSend:Ljava/util/ArrayList;
        //   263: aload_0        
        //   264: aconst_null    
        //   265: putfield        org/telegram/ui/LaunchActivity.contactsToSendUri:Landroid/net/Uri;
        //   268: iload           5
        //   270: ldc_w           1048576
        //   273: iand           
        //   274: ifne            6140
        //   277: aload_1        
        //   278: ifnull          6140
        //   281: aload_1        
        //   282: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //   285: ifnull          6140
        //   288: iload_3        
        //   289: ifne            6140
        //   292: ldc_w           "android.intent.action.SEND"
        //   295: aload_1        
        //   296: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //   299: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   302: istore_3       
        //   303: ldc_w           ""
        //   306: astore          9
        //   308: iload_3        
        //   309: ifeq            1097
        //   312: aload_1        
        //   313: invokevirtual   android/content/Intent.getType:()Ljava/lang/String;
        //   316: astore          10
        //   318: aload           10
        //   320: ifnull          392
        //   323: aload           10
        //   325: ldc_w           "text/x-vcard"
        //   328: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   331: ifeq            392
        //   334: aload_1        
        //   335: invokevirtual   android/content/Intent.getExtras:()Landroid/os/Bundle;
        //   338: ldc_w           "android.intent.extra.STREAM"
        //   341: invokevirtual   android/os/Bundle.get:(Ljava/lang/String;)Ljava/lang/Object;
        //   344: checkcast       Landroid/net/Uri;
        //   347: astore          9
        //   349: aload           9
        //   351: ifnull          386
        //   354: aload_0        
        //   355: aload           9
        //   357: aload_0        
        //   358: getfield        org/telegram/ui/LaunchActivity.currentAccount:I
        //   361: iconst_0       
        //   362: aconst_null    
        //   363: aconst_null    
        //   364: invokestatic    org/telegram/messenger/AndroidUtilities.loadVCardFromStream:(Landroid/net/Uri;IZLjava/util/ArrayList;Ljava/lang/String;)Ljava/util/ArrayList;
        //   367: putfield        org/telegram/ui/LaunchActivity.contactsToSend:Ljava/util/ArrayList;
        //   370: aload_0        
        //   371: aload           9
        //   373: putfield        org/telegram/ui/LaunchActivity.contactsToSendUri:Landroid/net/Uri;
        //   376: goto            1075
        //   379: astore          9
        //   381: aload           9
        //   383: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   386: iconst_1       
        //   387: istore          5
        //   389: goto            1078
        //   392: aload_1        
        //   393: ldc_w           "android.intent.extra.TEXT"
        //   396: invokevirtual   android/content/Intent.getStringExtra:(Ljava/lang/String;)Ljava/lang/String;
        //   399: astore          11
        //   401: aload           11
        //   403: astore          9
        //   405: aload           11
        //   407: ifnonnull       437
        //   410: aload_1        
        //   411: ldc_w           "android.intent.extra.TEXT"
        //   414: invokevirtual   android/content/Intent.getCharSequenceExtra:(Ljava/lang/String;)Ljava/lang/CharSequence;
        //   417: astore          12
        //   419: aload           11
        //   421: astore          9
        //   423: aload           12
        //   425: ifnull          437
        //   428: aload           12
        //   430: invokeinterface java/lang/CharSequence.toString:()Ljava/lang/String;
        //   435: astore          9
        //   437: aload_1        
        //   438: ldc_w           "android.intent.extra.SUBJECT"
        //   441: invokevirtual   android/content/Intent.getStringExtra:(Ljava/lang/String;)Ljava/lang/String;
        //   444: astore          12
        //   446: aload           9
        //   448: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   451: ifne            732
        //   454: aload_0        
        //   455: getfield        org/telegram/ui/LaunchActivity.locationRegex:Ljava/util/regex/Pattern;
        //   458: aload           9
        //   460: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //   463: astore          13
        //   465: aload           13
        //   467: invokevirtual   java/util/regex/Matcher.find:()Z
        //   470: ifeq            644
        //   473: aload           9
        //   475: ldc_w           "\\n"
        //   478: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   481: astore          11
        //   483: aload           11
        //   485: iconst_0       
        //   486: aaload         
        //   487: ldc_w           "My Position"
        //   490: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   493: ifeq            499
        //   496: goto            546
        //   499: aload           11
        //   501: iconst_0       
        //   502: aaload         
        //   503: ldc_w           "geo:"
        //   506: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   509: ifne            546
        //   512: aload           11
        //   514: iconst_0       
        //   515: aaload         
        //   516: astore          12
        //   518: aload           11
        //   520: iconst_1       
        //   521: aaload         
        //   522: ldc_w           "geo:"
        //   525: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   528: ifne            540
        //   531: aload           11
        //   533: iconst_1       
        //   534: aaload         
        //   535: astore          11
        //   537: goto            553
        //   540: aconst_null    
        //   541: astore          11
        //   543: goto            553
        //   546: aconst_null    
        //   547: astore          11
        //   549: aload           11
        //   551: astore          12
        //   553: aload_0        
        //   554: new             Landroid/location/Location;
        //   557: dup            
        //   558: ldc_w           ""
        //   561: invokespecial   android/location/Location.<init>:(Ljava/lang/String;)V
        //   564: putfield        org/telegram/ui/LaunchActivity.sendingLocation:Landroid/location/Location;
        //   567: aload_0        
        //   568: getfield        org/telegram/ui/LaunchActivity.sendingLocation:Landroid/location/Location;
        //   571: aload           13
        //   573: iconst_1       
        //   574: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   577: invokestatic    java/lang/Double.parseDouble:(Ljava/lang/String;)D
        //   580: invokevirtual   android/location/Location.setLatitude:(D)V
        //   583: aload_0        
        //   584: getfield        org/telegram/ui/LaunchActivity.sendingLocation:Landroid/location/Location;
        //   587: aload           13
        //   589: iconst_2       
        //   590: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   593: invokestatic    java/lang/Double.parseDouble:(Ljava/lang/String;)D
        //   596: invokevirtual   android/location/Location.setLongitude:(D)V
        //   599: new             Landroid/os/Bundle;
        //   602: dup            
        //   603: invokespecial   android/os/Bundle.<init>:()V
        //   606: astore          13
        //   608: aload           13
        //   610: ldc_w           "venueTitle"
        //   613: aload           12
        //   615: invokevirtual   android/os/Bundle.putCharSequence:(Ljava/lang/String;Ljava/lang/CharSequence;)V
        //   618: aload           13
        //   620: ldc_w           "venueAddress"
        //   623: aload           11
        //   625: invokevirtual   android/os/Bundle.putCharSequence:(Ljava/lang/String;Ljava/lang/CharSequence;)V
        //   628: aload_0        
        //   629: getfield        org/telegram/ui/LaunchActivity.sendingLocation:Landroid/location/Location;
        //   632: aload           13
        //   634: invokevirtual   android/location/Location.setExtras:(Landroid/os/Bundle;)V
        //   637: aload           9
        //   639: astore          11
        //   641: goto            723
        //   644: aload           9
        //   646: ldc_w           "http://"
        //   649: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   652: ifne            670
        //   655: aload           9
        //   657: astore          11
        //   659: aload           9
        //   661: ldc_w           "https://"
        //   664: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   667: ifeq            723
        //   670: aload           9
        //   672: astore          11
        //   674: aload           12
        //   676: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   679: ifne            723
        //   682: new             Ljava/lang/StringBuilder;
        //   685: dup            
        //   686: invokespecial   java/lang/StringBuilder.<init>:()V
        //   689: astore          11
        //   691: aload           11
        //   693: aload           12
        //   695: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   698: pop            
        //   699: aload           11
        //   701: ldc_w           "\n"
        //   704: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   707: pop            
        //   708: aload           11
        //   710: aload           9
        //   712: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   715: pop            
        //   716: aload           11
        //   718: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   721: astore          11
        //   723: aload_0        
        //   724: aload           11
        //   726: putfield        org/telegram/ui/LaunchActivity.sendingText:Ljava/lang/String;
        //   729: goto            746
        //   732: aload           12
        //   734: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   737: ifne            746
        //   740: aload_0        
        //   741: aload           12
        //   743: putfield        org/telegram/ui/LaunchActivity.sendingText:Ljava/lang/String;
        //   746: aload_1        
        //   747: ldc_w           "android.intent.extra.STREAM"
        //   750: invokevirtual   android/content/Intent.getParcelableExtra:(Ljava/lang/String;)Landroid/os/Parcelable;
        //   753: astore          11
        //   755: aload           11
        //   757: ifnull          1058
        //   760: aload           11
        //   762: astore          9
        //   764: aload           11
        //   766: instanceof      Landroid/net/Uri;
        //   769: ifne            782
        //   772: aload           11
        //   774: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   777: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //   780: astore          9
        //   782: aload           9
        //   784: checkcast       Landroid/net/Uri;
        //   787: astore          12
        //   789: aload           12
        //   791: ifnull          808
        //   794: aload           12
        //   796: invokestatic    org/telegram/messenger/AndroidUtilities.isInternalUri:(Landroid/net/Uri;)Z
        //   799: ifeq            808
        //   802: iconst_1       
        //   803: istore          5
        //   805: goto            811
        //   808: iconst_0       
        //   809: istore          5
        //   811: iload           5
        //   813: ifne            1055
        //   816: aload           12
        //   818: ifnull          901
        //   821: aload           10
        //   823: ifnull          837
        //   826: aload           10
        //   828: ldc_w           "image/"
        //   831: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   834: ifne            854
        //   837: aload           12
        //   839: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //   842: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   845: ldc_w           ".jpg"
        //   848: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //   851: ifeq            901
        //   854: aload_0        
        //   855: getfield        org/telegram/ui/LaunchActivity.photoPathsArray:Ljava/util/ArrayList;
        //   858: ifnonnull       872
        //   861: aload_0        
        //   862: new             Ljava/util/ArrayList;
        //   865: dup            
        //   866: invokespecial   java/util/ArrayList.<init>:()V
        //   869: putfield        org/telegram/ui/LaunchActivity.photoPathsArray:Ljava/util/ArrayList;
        //   872: new             Lorg/telegram/messenger/SendMessagesHelper$SendingMediaInfo;
        //   875: dup            
        //   876: invokespecial   org/telegram/messenger/SendMessagesHelper$SendingMediaInfo.<init>:()V
        //   879: astore          9
        //   881: aload           9
        //   883: aload           12
        //   885: putfield        org/telegram/messenger/SendMessagesHelper$SendingMediaInfo.uri:Landroid/net/Uri;
        //   888: aload_0        
        //   889: getfield        org/telegram/ui/LaunchActivity.photoPathsArray:Ljava/util/ArrayList;
        //   892: aload           9
        //   894: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   897: pop            
        //   898: goto            1055
        //   901: aload           12
        //   903: invokestatic    org/telegram/messenger/AndroidUtilities.getPath:(Landroid/net/Uri;)Ljava/lang/String;
        //   906: astore          11
        //   908: aload           11
        //   910: ifnull          1021
        //   913: aload           11
        //   915: astore          9
        //   917: aload           11
        //   919: ldc_w           "file:"
        //   922: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   925: ifeq            941
        //   928: aload           11
        //   930: ldc_w           "file://"
        //   933: ldc_w           ""
        //   936: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //   939: astore          9
        //   941: aload           10
        //   943: ifnull          966
        //   946: aload           10
        //   948: ldc_w           "video/"
        //   951: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   954: ifeq            966
        //   957: aload_0        
        //   958: aload           9
        //   960: putfield        org/telegram/ui/LaunchActivity.videoPath:Ljava/lang/String;
        //   963: goto            1055
        //   966: aload_0        
        //   967: getfield        org/telegram/ui/LaunchActivity.documentsPathsArray:Ljava/util/ArrayList;
        //   970: ifnonnull       995
        //   973: aload_0        
        //   974: new             Ljava/util/ArrayList;
        //   977: dup            
        //   978: invokespecial   java/util/ArrayList.<init>:()V
        //   981: putfield        org/telegram/ui/LaunchActivity.documentsPathsArray:Ljava/util/ArrayList;
        //   984: aload_0        
        //   985: new             Ljava/util/ArrayList;
        //   988: dup            
        //   989: invokespecial   java/util/ArrayList.<init>:()V
        //   992: putfield        org/telegram/ui/LaunchActivity.documentsOriginalPathsArray:Ljava/util/ArrayList;
        //   995: aload_0        
        //   996: getfield        org/telegram/ui/LaunchActivity.documentsPathsArray:Ljava/util/ArrayList;
        //   999: aload           9
        //  1001: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1004: pop            
        //  1005: aload_0        
        //  1006: getfield        org/telegram/ui/LaunchActivity.documentsOriginalPathsArray:Ljava/util/ArrayList;
        //  1009: aload           12
        //  1011: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //  1014: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1017: pop            
        //  1018: goto            1055
        //  1021: aload_0        
        //  1022: getfield        org/telegram/ui/LaunchActivity.documentsUrisArray:Ljava/util/ArrayList;
        //  1025: ifnonnull       1039
        //  1028: aload_0        
        //  1029: new             Ljava/util/ArrayList;
        //  1032: dup            
        //  1033: invokespecial   java/util/ArrayList.<init>:()V
        //  1036: putfield        org/telegram/ui/LaunchActivity.documentsUrisArray:Ljava/util/ArrayList;
        //  1039: aload_0        
        //  1040: getfield        org/telegram/ui/LaunchActivity.documentsUrisArray:Ljava/util/ArrayList;
        //  1043: aload           12
        //  1045: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1048: pop            
        //  1049: aload_0        
        //  1050: aload           10
        //  1052: putfield        org/telegram/ui/LaunchActivity.documentsMimeType:Ljava/lang/String;
        //  1055: goto            1078
        //  1058: aload_0        
        //  1059: getfield        org/telegram/ui/LaunchActivity.sendingText:Ljava/lang/String;
        //  1062: ifnonnull       1075
        //  1065: aload_0        
        //  1066: getfield        org/telegram/ui/LaunchActivity.sendingLocation:Landroid/location/Location;
        //  1069: ifnonnull       1075
        //  1072: goto            386
        //  1075: iconst_0       
        //  1076: istore          5
        //  1078: iload           5
        //  1080: ifeq            1631
        //  1083: aload_0        
        //  1084: ldc_w           "Unsupported content"
        //  1087: iconst_0       
        //  1088: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
        //  1091: invokevirtual   android/widget/Toast.show:()V
        //  1094: goto            1631
        //  1097: ldc_w           "android.intent.action.SEND_MULTIPLE"
        //  1100: aload_1        
        //  1101: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //  1104: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1107: ifeq            1634
        //  1110: aload_1        
        //  1111: ldc_w           "android.intent.extra.STREAM"
        //  1114: invokevirtual   android/content/Intent.getParcelableArrayListExtra:(Ljava/lang/String;)Ljava/util/ArrayList;
        //  1117: astore          11
        //  1119: aload_1        
        //  1120: invokevirtual   android/content/Intent.getType:()Ljava/lang/String;
        //  1123: astore          13
        //  1125: aload           11
        //  1127: astore          9
        //  1129: aload           11
        //  1131: ifnull          1247
        //  1134: iconst_0       
        //  1135: istore          5
        //  1137: iload           5
        //  1139: aload           11
        //  1141: invokevirtual   java/util/ArrayList.size:()I
        //  1144: if_icmpge       1232
        //  1147: aload           11
        //  1149: iload           5
        //  1151: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1154: checkcast       Landroid/os/Parcelable;
        //  1157: astore          12
        //  1159: aload           12
        //  1161: astore          9
        //  1163: aload           12
        //  1165: instanceof      Landroid/net/Uri;
        //  1168: ifne            1181
        //  1171: aload           12
        //  1173: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  1176: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  1179: astore          9
        //  1181: aload           9
        //  1183: checkcast       Landroid/net/Uri;
        //  1186: astore          9
        //  1188: iload           5
        //  1190: istore          14
        //  1192: aload           9
        //  1194: ifnull          1223
        //  1197: iload           5
        //  1199: istore          14
        //  1201: aload           9
        //  1203: invokestatic    org/telegram/messenger/AndroidUtilities.isInternalUri:(Landroid/net/Uri;)Z
        //  1206: ifeq            1223
        //  1209: aload           11
        //  1211: iload           5
        //  1213: invokevirtual   java/util/ArrayList.remove:(I)Ljava/lang/Object;
        //  1216: pop            
        //  1217: iload           5
        //  1219: iconst_1       
        //  1220: isub           
        //  1221: istore          14
        //  1223: iload           14
        //  1225: iconst_1       
        //  1226: iadd           
        //  1227: istore          5
        //  1229: goto            1137
        //  1232: aload           11
        //  1234: astore          9
        //  1236: aload           11
        //  1238: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  1241: ifeq            1247
        //  1244: aconst_null    
        //  1245: astore          9
        //  1247: aload           9
        //  1249: ifnull          1612
        //  1252: aload           13
        //  1254: ifnull          1378
        //  1257: aload           13
        //  1259: ldc_w           "image/"
        //  1262: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1265: ifeq            1378
        //  1268: iconst_0       
        //  1269: istore          5
        //  1271: iload           5
        //  1273: aload           9
        //  1275: invokevirtual   java/util/ArrayList.size:()I
        //  1278: if_icmpge       1599
        //  1281: aload           9
        //  1283: iload           5
        //  1285: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1288: checkcast       Landroid/os/Parcelable;
        //  1291: astore          12
        //  1293: aload           12
        //  1295: astore          11
        //  1297: aload           12
        //  1299: instanceof      Landroid/net/Uri;
        //  1302: ifne            1315
        //  1305: aload           12
        //  1307: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  1310: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  1313: astore          11
        //  1315: aload           11
        //  1317: checkcast       Landroid/net/Uri;
        //  1320: astore          11
        //  1322: aload_0        
        //  1323: getfield        org/telegram/ui/LaunchActivity.photoPathsArray:Ljava/util/ArrayList;
        //  1326: ifnonnull       1345
        //  1329: new             Ljava/util/ArrayList;
        //  1332: astore          12
        //  1334: aload           12
        //  1336: invokespecial   java/util/ArrayList.<init>:()V
        //  1339: aload_0        
        //  1340: aload           12
        //  1342: putfield        org/telegram/ui/LaunchActivity.photoPathsArray:Ljava/util/ArrayList;
        //  1345: new             Lorg/telegram/messenger/SendMessagesHelper$SendingMediaInfo;
        //  1348: astore          12
        //  1350: aload           12
        //  1352: invokespecial   org/telegram/messenger/SendMessagesHelper$SendingMediaInfo.<init>:()V
        //  1355: aload           12
        //  1357: aload           11
        //  1359: putfield        org/telegram/messenger/SendMessagesHelper$SendingMediaInfo.uri:Landroid/net/Uri;
        //  1362: aload_0        
        //  1363: getfield        org/telegram/ui/LaunchActivity.photoPathsArray:Ljava/util/ArrayList;
        //  1366: aload           12
        //  1368: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1371: pop            
        //  1372: iinc            5, 1
        //  1375: goto            1271
        //  1378: iconst_0       
        //  1379: istore          5
        //  1381: iload           5
        //  1383: aload           9
        //  1385: invokevirtual   java/util/ArrayList.size:()I
        //  1388: if_icmpge       1599
        //  1391: aload           9
        //  1393: iload           5
        //  1395: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1398: checkcast       Landroid/os/Parcelable;
        //  1401: astore          12
        //  1403: aload           12
        //  1405: astore          11
        //  1407: aload           12
        //  1409: instanceof      Landroid/net/Uri;
        //  1412: ifne            1425
        //  1415: aload           12
        //  1417: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  1420: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  1423: astore          11
        //  1425: aload           11
        //  1427: checkcast       Landroid/net/Uri;
        //  1430: astore          15
        //  1432: aload           15
        //  1434: invokestatic    org/telegram/messenger/AndroidUtilities.getPath:(Landroid/net/Uri;)Ljava/lang/String;
        //  1437: astore          12
        //  1439: aload           11
        //  1441: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  1444: astore          10
        //  1446: aload           10
        //  1448: astore          11
        //  1450: aload           10
        //  1452: ifnonnull       1459
        //  1455: aload           12
        //  1457: astore          11
        //  1459: aload           12
        //  1461: ifnull          1554
        //  1464: aload           12
        //  1466: astore          10
        //  1468: aload           12
        //  1470: ldc_w           "file:"
        //  1473: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1476: ifeq            1492
        //  1479: aload           12
        //  1481: ldc_w           "file://"
        //  1484: ldc_w           ""
        //  1487: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  1490: astore          10
        //  1492: aload_0        
        //  1493: getfield        org/telegram/ui/LaunchActivity.documentsPathsArray:Ljava/util/ArrayList;
        //  1496: ifnonnull       1531
        //  1499: new             Ljava/util/ArrayList;
        //  1502: astore          12
        //  1504: aload           12
        //  1506: invokespecial   java/util/ArrayList.<init>:()V
        //  1509: aload_0        
        //  1510: aload           12
        //  1512: putfield        org/telegram/ui/LaunchActivity.documentsPathsArray:Ljava/util/ArrayList;
        //  1515: new             Ljava/util/ArrayList;
        //  1518: astore          12
        //  1520: aload           12
        //  1522: invokespecial   java/util/ArrayList.<init>:()V
        //  1525: aload_0        
        //  1526: aload           12
        //  1528: putfield        org/telegram/ui/LaunchActivity.documentsOriginalPathsArray:Ljava/util/ArrayList;
        //  1531: aload_0        
        //  1532: getfield        org/telegram/ui/LaunchActivity.documentsPathsArray:Ljava/util/ArrayList;
        //  1535: aload           10
        //  1537: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1540: pop            
        //  1541: aload_0        
        //  1542: getfield        org/telegram/ui/LaunchActivity.documentsOriginalPathsArray:Ljava/util/ArrayList;
        //  1545: aload           11
        //  1547: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1550: pop            
        //  1551: goto            1593
        //  1554: aload_0        
        //  1555: getfield        org/telegram/ui/LaunchActivity.documentsUrisArray:Ljava/util/ArrayList;
        //  1558: ifnonnull       1577
        //  1561: new             Ljava/util/ArrayList;
        //  1564: astore          11
        //  1566: aload           11
        //  1568: invokespecial   java/util/ArrayList.<init>:()V
        //  1571: aload_0        
        //  1572: aload           11
        //  1574: putfield        org/telegram/ui/LaunchActivity.documentsUrisArray:Ljava/util/ArrayList;
        //  1577: aload_0        
        //  1578: getfield        org/telegram/ui/LaunchActivity.documentsUrisArray:Ljava/util/ArrayList;
        //  1581: aload           15
        //  1583: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1586: pop            
        //  1587: aload_0        
        //  1588: aload           13
        //  1590: putfield        org/telegram/ui/LaunchActivity.documentsMimeType:Ljava/lang/String;
        //  1593: iinc            5, 1
        //  1596: goto            1381
        //  1599: iconst_0       
        //  1600: istore          5
        //  1602: goto            1615
        //  1605: astore          9
        //  1607: aload           9
        //  1609: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1612: iconst_1       
        //  1613: istore          5
        //  1615: iload           5
        //  1617: ifeq            1631
        //  1620: aload_0        
        //  1621: ldc_w           "Unsupported content"
        //  1624: iconst_0       
        //  1625: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
        //  1628: invokevirtual   android/widget/Toast.show:()V
        //  1631: goto            6140
        //  1634: ldc_w           "android.intent.action.VIEW"
        //  1637: aload_1        
        //  1638: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //  1641: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1644: ifeq            5855
        //  1647: aload_1        
        //  1648: invokevirtual   android/content/Intent.getData:()Landroid/net/Uri;
        //  1651: astore          13
        //  1653: aload           13
        //  1655: ifnull          5811
        //  1658: aload           13
        //  1660: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
        //  1663: astore          11
        //  1665: aload           11
        //  1667: ifnull          5228
        //  1670: aload           11
        //  1672: ldc_w           "http"
        //  1675: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1678: ifne            3783
        //  1681: aload           11
        //  1683: ldc_w           "https"
        //  1686: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1689: ifeq            1695
        //  1692: goto            3783
        //  1695: aload           11
        //  1697: ldc_w           "tg"
        //  1700: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1703: ifeq            5228
        //  1706: aload           13
        //  1708: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //  1711: astore          11
        //  1713: aload           11
        //  1715: ldc_w           "tg:resolve"
        //  1718: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1721: ifne            3393
        //  1724: aload           11
        //  1726: ldc_w           "tg://resolve"
        //  1729: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1732: ifeq            1738
        //  1735: goto            3393
        //  1738: aload           11
        //  1740: ldc_w           "tg:privatepost"
        //  1743: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1746: ifne            3262
        //  1749: aload           11
        //  1751: ldc_w           "tg://privatepost"
        //  1754: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1757: ifeq            1763
        //  1760: goto            3262
        //  1763: aload           11
        //  1765: ldc_w           "tg:bg"
        //  1768: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1771: ifne            2933
        //  1774: aload           11
        //  1776: ldc_w           "tg://bg"
        //  1779: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1782: ifeq            1788
        //  1785: goto            2933
        //  1788: aload           11
        //  1790: ldc_w           "tg:join"
        //  1793: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1796: ifne            2896
        //  1799: aload           11
        //  1801: ldc_w           "tg://join"
        //  1804: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1807: ifeq            1813
        //  1810: goto            2896
        //  1813: aload           11
        //  1815: ldc_w           "tg:addstickers"
        //  1818: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1821: ifne            2856
        //  1824: aload           11
        //  1826: ldc_w           "tg://addstickers"
        //  1829: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1832: ifeq            1838
        //  1835: goto            2856
        //  1838: aload           11
        //  1840: ldc_w           "tg:msg"
        //  1843: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1846: ifne            2619
        //  1849: aload           11
        //  1851: ldc_w           "tg://msg"
        //  1854: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1857: ifne            2619
        //  1860: aload           11
        //  1862: ldc_w           "tg://share"
        //  1865: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1868: ifne            2619
        //  1871: aload           11
        //  1873: ldc_w           "tg:share"
        //  1876: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1879: ifeq            1885
        //  1882: goto            2619
        //  1885: aload           11
        //  1887: ldc_w           "tg:confirmphone"
        //  1890: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1893: ifne            2557
        //  1896: aload           11
        //  1898: ldc_w           "tg://confirmphone"
        //  1901: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1904: ifeq            1910
        //  1907: goto            2557
        //  1910: aload           11
        //  1912: ldc_w           "tg:login"
        //  1915: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1918: ifne            2523
        //  1921: aload           11
        //  1923: ldc_w           "tg://login"
        //  1926: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1929: ifeq            1935
        //  1932: goto            2523
        //  1935: aload           11
        //  1937: ldc_w           "tg:openmessage"
        //  1940: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1943: ifne            2346
        //  1946: aload           11
        //  1948: ldc_w           "tg://openmessage"
        //  1951: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1954: ifeq            1960
        //  1957: goto            2346
        //  1960: aload           11
        //  1962: ldc_w           "tg:passport"
        //  1965: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1968: ifne            2161
        //  1971: aload           11
        //  1973: ldc_w           "tg://passport"
        //  1976: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1979: ifne            2161
        //  1982: aload           11
        //  1984: ldc_w           "tg:secureid"
        //  1987: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1990: ifeq            1996
        //  1993: goto            2161
        //  1996: aload           11
        //  1998: ldc_w           "tg:setlanguage"
        //  2001: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  2004: ifne            2101
        //  2007: aload           11
        //  2009: ldc_w           "tg://setlanguage"
        //  2012: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  2015: ifeq            2021
        //  2018: goto            2101
        //  2021: aload           11
        //  2023: ldc_w           "tg://"
        //  2026: ldc_w           ""
        //  2029: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2032: ldc_w           "tg:"
        //  2035: ldc_w           ""
        //  2038: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2041: astore          11
        //  2043: aload           11
        //  2045: bipush          63
        //  2047: invokevirtual   java/lang/String.indexOf:(I)I
        //  2050: istore          5
        //  2052: aload           11
        //  2054: astore          9
        //  2056: iload           5
        //  2058: iflt            2071
        //  2061: aload           11
        //  2063: iconst_0       
        //  2064: iload           5
        //  2066: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  2069: astore          9
        //  2071: aload           9
        //  2073: astore          16
        //  2075: aconst_null    
        //  2076: astore          9
        //  2078: aconst_null    
        //  2079: astore          11
        //  2081: aconst_null    
        //  2082: astore          12
        //  2084: aconst_null    
        //  2085: astore          10
        //  2087: aconst_null    
        //  2088: astore          13
        //  2090: iconst_0       
        //  2091: istore_3       
        //  2092: aconst_null    
        //  2093: astore          17
        //  2095: aconst_null    
        //  2096: astore          18
        //  2098: goto            3626
        //  2101: aload           11
        //  2103: ldc_w           "tg:setlanguage"
        //  2106: ldc_w           "tg://telegram.org"
        //  2109: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2112: ldc_w           "tg://setlanguage"
        //  2115: ldc_w           "tg://telegram.org"
        //  2118: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2121: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  2124: ldc_w           "lang"
        //  2127: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2130: astore          19
        //  2132: aconst_null    
        //  2133: astore          9
        //  2135: aconst_null    
        //  2136: astore          11
        //  2138: aconst_null    
        //  2139: astore          12
        //  2141: aconst_null    
        //  2142: astore          10
        //  2144: aconst_null    
        //  2145: astore          13
        //  2147: iconst_0       
        //  2148: istore_3       
        //  2149: aconst_null    
        //  2150: astore          17
        //  2152: aconst_null    
        //  2153: astore          18
        //  2155: aconst_null    
        //  2156: astore          16
        //  2158: goto            3629
        //  2161: aload           11
        //  2163: ldc_w           "tg:passport"
        //  2166: ldc_w           "tg://telegram.org"
        //  2169: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2172: ldc_w           "tg://passport"
        //  2175: ldc_w           "tg://telegram.org"
        //  2178: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2181: ldc_w           "tg:secureid"
        //  2184: ldc_w           "tg://telegram.org"
        //  2187: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2190: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  2193: astore          12
        //  2195: new             Ljava/util/HashMap;
        //  2198: dup            
        //  2199: invokespecial   java/util/HashMap.<init>:()V
        //  2202: astore          9
        //  2204: aload           12
        //  2206: ldc_w           "scope"
        //  2209: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2212: astore          11
        //  2214: aload           11
        //  2216: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  2219: ifne            2264
        //  2222: aload           11
        //  2224: ldc_w           "{"
        //  2227: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  2230: ifeq            2264
        //  2233: aload           11
        //  2235: ldc_w           "}"
        //  2238: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //  2241: ifeq            2264
        //  2244: aload           9
        //  2246: ldc_w           "nonce"
        //  2249: aload           12
        //  2251: ldc_w           "nonce"
        //  2254: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2257: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2260: pop            
        //  2261: goto            2281
        //  2264: aload           9
        //  2266: ldc_w           "payload"
        //  2269: aload           12
        //  2271: ldc_w           "payload"
        //  2274: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2277: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2280: pop            
        //  2281: aload           9
        //  2283: ldc_w           "bot_id"
        //  2286: aload           12
        //  2288: ldc_w           "bot_id"
        //  2291: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2294: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2297: pop            
        //  2298: aload           9
        //  2300: ldc_w           "scope"
        //  2303: aload           11
        //  2305: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2308: pop            
        //  2309: aload           9
        //  2311: ldc_w           "public_key"
        //  2314: aload           12
        //  2316: ldc_w           "public_key"
        //  2319: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2322: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2325: pop            
        //  2326: aload           9
        //  2328: ldc_w           "callback_url"
        //  2331: aload           12
        //  2333: ldc_w           "callback_url"
        //  2336: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2339: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2342: pop            
        //  2343: goto            3587
        //  2346: aload           11
        //  2348: ldc_w           "tg:openmessage"
        //  2351: ldc_w           "tg://telegram.org"
        //  2354: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2357: ldc_w           "tg://openmessage"
        //  2360: ldc_w           "tg://telegram.org"
        //  2363: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2366: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  2369: astore          9
        //  2371: aload           9
        //  2373: ldc_w           "user_id"
        //  2376: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2379: astore          12
        //  2381: aload           9
        //  2383: ldc_w           "chat_id"
        //  2386: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2389: astore          11
        //  2391: aload           9
        //  2393: ldc_w           "message_id"
        //  2396: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2399: astore          9
        //  2401: aload           12
        //  2403: ifnull          2419
        //  2406: aload           12
        //  2408: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //  2411: istore          5
        //  2413: iconst_0       
        //  2414: istore          14
        //  2416: goto            2440
        //  2419: aload           11
        //  2421: ifnull          2434
        //  2424: aload           11
        //  2426: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //  2429: istore          14
        //  2431: goto            2437
        //  2434: iconst_0       
        //  2435: istore          14
        //  2437: iconst_0       
        //  2438: istore          5
        //  2440: aload           9
        //  2442: ifnull          2455
        //  2445: aload           9
        //  2447: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //  2450: istore          20
        //  2452: goto            2458
        //  2455: iconst_0       
        //  2456: istore          20
        //  2458: iload           20
        //  2460: istore          21
        //  2462: aconst_null    
        //  2463: astore          9
        //  2465: aconst_null    
        //  2466: astore          11
        //  2468: aconst_null    
        //  2469: astore          12
        //  2471: aconst_null    
        //  2472: astore          10
        //  2474: aconst_null    
        //  2475: astore          13
        //  2477: aconst_null    
        //  2478: astore          15
        //  2480: iconst_0       
        //  2481: istore_3       
        //  2482: aconst_null    
        //  2483: astore          22
        //  2485: aconst_null    
        //  2486: astore          17
        //  2488: aconst_null    
        //  2489: astore          18
        //  2491: aconst_null    
        //  2492: astore          16
        //  2494: aconst_null    
        //  2495: astore          19
        //  2497: aconst_null    
        //  2498: astore          23
        //  2500: aconst_null    
        //  2501: astore          24
        //  2503: aconst_null    
        //  2504: astore          25
        //  2506: aconst_null    
        //  2507: astore          26
        //  2509: aconst_null    
        //  2510: astore          27
        //  2512: iload           14
        //  2514: istore          20
        //  2516: iload           21
        //  2518: istore          14
        //  2520: goto            5302
        //  2523: aload           11
        //  2525: ldc_w           "tg:login"
        //  2528: ldc_w           "tg://telegram.org"
        //  2531: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2534: ldc_w           "tg://login"
        //  2537: ldc_w           "tg://telegram.org"
        //  2540: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2543: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  2546: ldc_w           "code"
        //  2549: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2552: astore          9
        //  2554: goto            3370
        //  2557: aload           11
        //  2559: ldc_w           "tg:confirmphone"
        //  2562: ldc_w           "tg://telegram.org"
        //  2565: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2568: ldc_w           "tg://confirmphone"
        //  2571: ldc_w           "tg://telegram.org"
        //  2574: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2577: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  2580: astore          9
        //  2582: aload           9
        //  2584: ldc_w           "phone"
        //  2587: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2590: astore          15
        //  2592: aload           9
        //  2594: ldc_w           "hash"
        //  2597: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2600: astore          13
        //  2602: aconst_null    
        //  2603: astore          9
        //  2605: aconst_null    
        //  2606: astore          11
        //  2608: aconst_null    
        //  2609: astore          12
        //  2611: aconst_null    
        //  2612: astore          10
        //  2614: iconst_0       
        //  2615: istore_3       
        //  2616: goto            3387
        //  2619: aload           11
        //  2621: ldc_w           "tg:msg"
        //  2624: ldc_w           "tg://telegram.org"
        //  2627: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2630: ldc_w           "tg://msg"
        //  2633: ldc_w           "tg://telegram.org"
        //  2636: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2639: ldc_w           "tg://share"
        //  2642: ldc_w           "tg://telegram.org"
        //  2645: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2648: ldc_w           "tg:share"
        //  2651: ldc_w           "tg://telegram.org"
        //  2654: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2657: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  2660: astore          12
        //  2662: aload           12
        //  2664: ldc_w           "url"
        //  2667: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2670: astore          11
        //  2672: aload           11
        //  2674: ifnonnull       2680
        //  2677: goto            2684
        //  2680: aload           11
        //  2682: astore          9
        //  2684: aload           12
        //  2686: ldc_w           "text"
        //  2689: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2692: ifnull          2784
        //  2695: aload           9
        //  2697: invokevirtual   java/lang/String.length:()I
        //  2700: ifle            2741
        //  2703: new             Ljava/lang/StringBuilder;
        //  2706: dup            
        //  2707: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2710: astore          11
        //  2712: aload           11
        //  2714: aload           9
        //  2716: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2719: pop            
        //  2720: aload           11
        //  2722: ldc_w           "\n"
        //  2725: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2728: pop            
        //  2729: aload           11
        //  2731: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2734: astore          9
        //  2736: iconst_1       
        //  2737: istore_3       
        //  2738: goto            2743
        //  2741: iconst_0       
        //  2742: istore_3       
        //  2743: new             Ljava/lang/StringBuilder;
        //  2746: dup            
        //  2747: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2750: astore          11
        //  2752: aload           11
        //  2754: aload           9
        //  2756: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2759: pop            
        //  2760: aload           11
        //  2762: aload           12
        //  2764: ldc_w           "text"
        //  2767: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2770: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2773: pop            
        //  2774: aload           11
        //  2776: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2779: astore          9
        //  2781: goto            2786
        //  2784: iconst_0       
        //  2785: istore_3       
        //  2786: aload           9
        //  2788: invokevirtual   java/lang/String.length:()I
        //  2791: sipush          16384
        //  2794: if_icmple       2811
        //  2797: aload           9
        //  2799: iconst_0       
        //  2800: sipush          16384
        //  2803: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  2806: astore          9
        //  2808: goto            2811
        //  2811: aload           9
        //  2813: ldc_w           "\n"
        //  2816: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //  2819: ifeq            2840
        //  2822: aload           9
        //  2824: iconst_0       
        //  2825: aload           9
        //  2827: invokevirtual   java/lang/String.length:()I
        //  2830: iconst_1       
        //  2831: isub           
        //  2832: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  2835: astore          9
        //  2837: goto            2811
        //  2840: aload           9
        //  2842: astore          11
        //  2844: aconst_null    
        //  2845: astore          9
        //  2847: aconst_null    
        //  2848: astore          12
        //  2850: aconst_null    
        //  2851: astore          10
        //  2853: goto            3381
        //  2856: aload           11
        //  2858: ldc_w           "tg:addstickers"
        //  2861: ldc_w           "tg://telegram.org"
        //  2864: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2867: ldc_w           "tg://addstickers"
        //  2870: ldc_w           "tg://telegram.org"
        //  2873: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2876: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  2879: ldc_w           "set"
        //  2882: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2885: astore          10
        //  2887: aconst_null    
        //  2888: astore          9
        //  2890: aconst_null    
        //  2891: astore          12
        //  2893: goto            3376
        //  2896: aload           11
        //  2898: ldc_w           "tg:join"
        //  2901: ldc_w           "tg://telegram.org"
        //  2904: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2907: ldc_w           "tg://join"
        //  2910: ldc_w           "tg://telegram.org"
        //  2913: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2916: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  2919: ldc_w           "invite"
        //  2922: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2925: astore          12
        //  2927: aconst_null    
        //  2928: astore          9
        //  2930: goto            3373
        //  2933: aload           11
        //  2935: ldc_w           "tg:bg"
        //  2938: ldc_w           "tg://telegram.org"
        //  2941: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2944: ldc_w           "tg://bg"
        //  2947: ldc_w           "tg://telegram.org"
        //  2950: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2953: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  2956: astore          9
        //  2958: new             Lorg/telegram/tgnet/TLRPC$TL_wallPaper;
        //  2961: dup            
        //  2962: invokespecial   org/telegram/tgnet/TLRPC$TL_wallPaper.<init>:()V
        //  2965: astore          23
        //  2967: aload           23
        //  2969: new             Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  2972: dup            
        //  2973: invokespecial   org/telegram/tgnet/TLRPC$TL_wallPaperSettings.<init>:()V
        //  2976: putfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  2979: aload           23
        //  2981: aload           9
        //  2983: ldc_w           "slug"
        //  2986: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  2989: putfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  2992: aload           23
        //  2994: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  2997: ifnonnull       3013
        //  3000: aload           23
        //  3002: aload           9
        //  3004: ldc_w           "color"
        //  3007: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3010: putfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  3013: aload           23
        //  3015: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  3018: astore          11
        //  3020: aload           11
        //  3022: ifnull          3066
        //  3025: aload           11
        //  3027: invokevirtual   java/lang/String.length:()I
        //  3030: bipush          6
        //  3032: if_icmpne       3066
        //  3035: aload           23
        //  3037: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  3040: aload           23
        //  3042: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  3045: bipush          16
        //  3047: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;I)I
        //  3050: ldc_w           -16777216
        //  3053: ior            
        //  3054: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.background_color:I
        //  3057: aload           23
        //  3059: aconst_null    
        //  3060: putfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  3063: goto            3230
        //  3066: aload           9
        //  3068: ldc_w           "mode"
        //  3071: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3074: astore          11
        //  3076: aload           11
        //  3078: ifnull          3171
        //  3081: aload           11
        //  3083: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //  3086: ldc_w           " "
        //  3089: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //  3092: astore          11
        //  3094: aload           11
        //  3096: ifnull          3171
        //  3099: aload           11
        //  3101: arraylength    
        //  3102: ifle            3171
        //  3105: iconst_0       
        //  3106: istore          5
        //  3108: iload           5
        //  3110: aload           11
        //  3112: arraylength    
        //  3113: if_icmpge       3171
        //  3116: ldc_w           "blur"
        //  3119: aload           11
        //  3121: iload           5
        //  3123: aaload         
        //  3124: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3127: ifeq            3142
        //  3130: aload           23
        //  3132: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  3135: iconst_1       
        //  3136: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.blur:Z
        //  3139: goto            3165
        //  3142: ldc_w           "motion"
        //  3145: aload           11
        //  3147: iload           5
        //  3149: aaload         
        //  3150: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3153: ifeq            3165
        //  3156: aload           23
        //  3158: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  3161: iconst_1       
        //  3162: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.motion:Z
        //  3165: iinc            5, 1
        //  3168: goto            3108
        //  3171: aload           23
        //  3173: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  3176: aload           9
        //  3178: ldc_w           "intensity"
        //  3181: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3184: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  3187: invokevirtual   java/lang/Integer.intValue:()I
        //  3190: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.intensity:I
        //  3193: aload           9
        //  3195: ldc_w           "bg_color"
        //  3198: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3201: astore          9
        //  3203: aload           9
        //  3205: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  3208: ifne            3230
        //  3211: aload           23
        //  3213: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  3216: aload           9
        //  3218: bipush          16
        //  3220: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;I)I
        //  3223: ldc_w           -16777216
        //  3226: ior            
        //  3227: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.background_color:I
        //  3230: aconst_null    
        //  3231: astore          9
        //  3233: aconst_null    
        //  3234: astore          11
        //  3236: aconst_null    
        //  3237: astore          12
        //  3239: aconst_null    
        //  3240: astore          10
        //  3242: aconst_null    
        //  3243: astore          13
        //  3245: iconst_0       
        //  3246: istore_3       
        //  3247: aconst_null    
        //  3248: astore          17
        //  3250: aconst_null    
        //  3251: astore          18
        //  3253: aconst_null    
        //  3254: astore          16
        //  3256: aconst_null    
        //  3257: astore          19
        //  3259: goto            3632
        //  3262: aload           11
        //  3264: ldc_w           "tg:privatepost"
        //  3267: ldc_w           "tg://telegram.org"
        //  3270: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  3273: ldc_w           "tg://privatepost"
        //  3276: ldc_w           "tg://telegram.org"
        //  3279: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  3282: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  3285: astore          9
        //  3287: aload           9
        //  3289: ldc_w           "post"
        //  3292: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3295: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  3298: astore          25
        //  3300: aload           9
        //  3302: ldc_w           "channel"
        //  3305: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3308: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  3311: astore          24
        //  3313: aload           25
        //  3315: invokevirtual   java/lang/Integer.intValue:()I
        //  3318: ifeq            3367
        //  3321: aload           24
        //  3323: invokevirtual   java/lang/Integer.intValue:()I
        //  3326: ifne            3332
        //  3329: goto            3367
        //  3332: aconst_null    
        //  3333: astore          9
        //  3335: aconst_null    
        //  3336: astore          11
        //  3338: aconst_null    
        //  3339: astore          12
        //  3341: aconst_null    
        //  3342: astore          10
        //  3344: aconst_null    
        //  3345: astore          13
        //  3347: iconst_0       
        //  3348: istore_3       
        //  3349: aconst_null    
        //  3350: astore          17
        //  3352: aconst_null    
        //  3353: astore          18
        //  3355: aconst_null    
        //  3356: astore          16
        //  3358: aconst_null    
        //  3359: astore          19
        //  3361: aconst_null    
        //  3362: astore          23
        //  3364: goto            3638
        //  3367: aconst_null    
        //  3368: astore          9
        //  3370: aconst_null    
        //  3371: astore          12
        //  3373: aconst_null    
        //  3374: astore          10
        //  3376: aconst_null    
        //  3377: astore          11
        //  3379: iconst_0       
        //  3380: istore_3       
        //  3381: aconst_null    
        //  3382: astore          15
        //  3384: aconst_null    
        //  3385: astore          13
        //  3387: aconst_null    
        //  3388: astore          18
        //  3390: goto            3615
        //  3393: aload           11
        //  3395: ldc_w           "tg:resolve"
        //  3398: ldc_w           "tg://telegram.org"
        //  3401: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  3404: ldc_w           "tg://resolve"
        //  3407: ldc_w           "tg://telegram.org"
        //  3410: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  3413: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  3416: astore          11
        //  3418: aload           11
        //  3420: ldc_w           "domain"
        //  3423: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3426: astore          15
        //  3428: ldc_w           "telegrampassport"
        //  3431: aload           15
        //  3433: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3436: ifeq            3653
        //  3439: new             Ljava/util/HashMap;
        //  3442: dup            
        //  3443: invokespecial   java/util/HashMap.<init>:()V
        //  3446: astore          9
        //  3448: aload           11
        //  3450: ldc_w           "scope"
        //  3453: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3456: astore          12
        //  3458: aload           12
        //  3460: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  3463: ifne            3508
        //  3466: aload           12
        //  3468: ldc_w           "{"
        //  3471: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  3474: ifeq            3508
        //  3477: aload           12
        //  3479: ldc_w           "}"
        //  3482: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //  3485: ifeq            3508
        //  3488: aload           9
        //  3490: ldc_w           "nonce"
        //  3493: aload           11
        //  3495: ldc_w           "nonce"
        //  3498: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3501: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3504: pop            
        //  3505: goto            3525
        //  3508: aload           9
        //  3510: ldc_w           "payload"
        //  3513: aload           11
        //  3515: ldc_w           "payload"
        //  3518: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3521: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3524: pop            
        //  3525: aload           9
        //  3527: ldc_w           "bot_id"
        //  3530: aload           11
        //  3532: ldc_w           "bot_id"
        //  3535: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3538: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3541: pop            
        //  3542: aload           9
        //  3544: ldc_w           "scope"
        //  3547: aload           12
        //  3549: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3552: pop            
        //  3553: aload           9
        //  3555: ldc_w           "public_key"
        //  3558: aload           11
        //  3560: ldc_w           "public_key"
        //  3563: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3566: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3569: pop            
        //  3570: aload           9
        //  3572: ldc_w           "callback_url"
        //  3575: aload           11
        //  3577: ldc_w           "callback_url"
        //  3580: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3583: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3586: pop            
        //  3587: aconst_null    
        //  3588: astore          22
        //  3590: aconst_null    
        //  3591: astore          11
        //  3593: aconst_null    
        //  3594: astore          12
        //  3596: aconst_null    
        //  3597: astore          10
        //  3599: aconst_null    
        //  3600: astore          15
        //  3602: iconst_0       
        //  3603: istore_3       
        //  3604: aconst_null    
        //  3605: astore          13
        //  3607: aload           9
        //  3609: astore          18
        //  3611: aload           22
        //  3613: astore          9
        //  3615: aconst_null    
        //  3616: astore          16
        //  3618: aload           13
        //  3620: astore          17
        //  3622: aload           15
        //  3624: astore          13
        //  3626: aconst_null    
        //  3627: astore          19
        //  3629: aconst_null    
        //  3630: astore          23
        //  3632: aconst_null    
        //  3633: astore          24
        //  3635: aconst_null    
        //  3636: astore          25
        //  3638: aconst_null    
        //  3639: astore          22
        //  3641: aconst_null    
        //  3642: astore          15
        //  3644: aconst_null    
        //  3645: astore          26
        //  3647: aconst_null    
        //  3648: astore          27
        //  3650: goto            5293
        //  3653: aload           11
        //  3655: ldc_w           "start"
        //  3658: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3661: astore          26
        //  3663: aload           11
        //  3665: ldc_w           "startgroup"
        //  3668: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3671: astore          27
        //  3673: aload           11
        //  3675: ldc_w           "game"
        //  3678: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3681: astore          22
        //  3683: aload           11
        //  3685: ldc_w           "post"
        //  3688: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3691: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  3694: astore          25
        //  3696: aload           25
        //  3698: invokevirtual   java/lang/Integer.intValue:()I
        //  3701: ifne            3745
        //  3704: aconst_null    
        //  3705: astore          9
        //  3707: aconst_null    
        //  3708: astore          11
        //  3710: aconst_null    
        //  3711: astore          12
        //  3713: aconst_null    
        //  3714: astore          10
        //  3716: aconst_null    
        //  3717: astore          13
        //  3719: iconst_0       
        //  3720: istore_3       
        //  3721: aconst_null    
        //  3722: astore          17
        //  3724: aconst_null    
        //  3725: astore          18
        //  3727: aconst_null    
        //  3728: astore          16
        //  3730: aconst_null    
        //  3731: astore          19
        //  3733: aconst_null    
        //  3734: astore          23
        //  3736: aconst_null    
        //  3737: astore          24
        //  3739: aconst_null    
        //  3740: astore          25
        //  3742: goto            5293
        //  3745: aconst_null    
        //  3746: astore          9
        //  3748: aconst_null    
        //  3749: astore          11
        //  3751: aconst_null    
        //  3752: astore          12
        //  3754: aconst_null    
        //  3755: astore          10
        //  3757: aconst_null    
        //  3758: astore          13
        //  3760: iconst_0       
        //  3761: istore_3       
        //  3762: aconst_null    
        //  3763: astore          17
        //  3765: aconst_null    
        //  3766: astore          18
        //  3768: aconst_null    
        //  3769: astore          16
        //  3771: aconst_null    
        //  3772: astore          19
        //  3774: aconst_null    
        //  3775: astore          23
        //  3777: aconst_null    
        //  3778: astore          24
        //  3780: goto            5293
        //  3783: aload           13
        //  3785: invokevirtual   android/net/Uri.getHost:()Ljava/lang/String;
        //  3788: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //  3791: astore          11
        //  3793: aload           11
        //  3795: ldc_w           "telegram.me"
        //  3798: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3801: ifne            3826
        //  3804: aload           11
        //  3806: ldc_w           "t.me"
        //  3809: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3812: ifne            3826
        //  3815: aload           11
        //  3817: ldc_w           "telegram.dog"
        //  3820: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3823: ifeq            5048
        //  3826: aload           13
        //  3828: invokevirtual   android/net/Uri.getPath:()Ljava/lang/String;
        //  3831: astore          11
        //  3833: aload           11
        //  3835: ifnull          5048
        //  3838: aload           11
        //  3840: invokevirtual   java/lang/String.length:()I
        //  3843: iconst_1       
        //  3844: if_icmple       5048
        //  3847: aload           11
        //  3849: iconst_1       
        //  3850: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //  3853: astore          11
        //  3855: aload           11
        //  3857: ldc_w           "bg/"
        //  3860: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  3863: ifeq            4207
        //  3866: new             Lorg/telegram/tgnet/TLRPC$TL_wallPaper;
        //  3869: dup            
        //  3870: invokespecial   org/telegram/tgnet/TLRPC$TL_wallPaper.<init>:()V
        //  3873: astore          23
        //  3875: aload           23
        //  3877: new             Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  3880: dup            
        //  3881: invokespecial   org/telegram/tgnet/TLRPC$TL_wallPaperSettings.<init>:()V
        //  3884: putfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  3887: aload           23
        //  3889: aload           11
        //  3891: ldc_w           "bg/"
        //  3894: ldc_w           ""
        //  3897: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  3900: putfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  3903: aload           23
        //  3905: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  3908: astore          9
        //  3910: aload           9
        //  3912: ifnull          3956
        //  3915: aload           9
        //  3917: invokevirtual   java/lang/String.length:()I
        //  3920: bipush          6
        //  3922: if_icmpne       3956
        //  3925: aload           23
        //  3927: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  3930: aload           23
        //  3932: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  3935: bipush          16
        //  3937: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;I)I
        //  3940: ldc_w           -16777216
        //  3943: ior            
        //  3944: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.background_color:I
        //  3947: aload           23
        //  3949: aconst_null    
        //  3950: putfield        org/telegram/tgnet/TLRPC$TL_wallPaper.slug:Ljava/lang/String;
        //  3953: goto            4157
        //  3956: aload           13
        //  3958: ldc_w           "mode"
        //  3961: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  3964: astore          9
        //  3966: aload           9
        //  3968: ifnull          4061
        //  3971: aload           9
        //  3973: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //  3976: ldc_w           " "
        //  3979: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //  3982: astore          9
        //  3984: aload           9
        //  3986: ifnull          4061
        //  3989: aload           9
        //  3991: arraylength    
        //  3992: ifle            4061
        //  3995: iconst_0       
        //  3996: istore          5
        //  3998: iload           5
        //  4000: aload           9
        //  4002: arraylength    
        //  4003: if_icmpge       4061
        //  4006: ldc_w           "blur"
        //  4009: aload           9
        //  4011: iload           5
        //  4013: aaload         
        //  4014: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  4017: ifeq            4032
        //  4020: aload           23
        //  4022: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  4025: iconst_1       
        //  4026: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.blur:Z
        //  4029: goto            4055
        //  4032: ldc_w           "motion"
        //  4035: aload           9
        //  4037: iload           5
        //  4039: aaload         
        //  4040: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  4043: ifeq            4055
        //  4046: aload           23
        //  4048: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  4051: iconst_1       
        //  4052: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.motion:Z
        //  4055: iinc            5, 1
        //  4058: goto            3998
        //  4061: aload           13
        //  4063: ldc_w           "intensity"
        //  4066: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4069: astore          9
        //  4071: aload           9
        //  4073: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  4076: ifne            4098
        //  4079: aload           23
        //  4081: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  4084: aload           9
        //  4086: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  4089: invokevirtual   java/lang/Integer.intValue:()I
        //  4092: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.intensity:I
        //  4095: goto            4108
        //  4098: aload           23
        //  4100: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  4103: bipush          50
        //  4105: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.intensity:I
        //  4108: aload           13
        //  4110: ldc_w           "bg_color"
        //  4113: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4116: astore          9
        //  4118: aload           9
        //  4120: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  4123: ifne            4148
        //  4126: aload           23
        //  4128: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  4131: aload           9
        //  4133: bipush          16
        //  4135: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;I)I
        //  4138: ldc_w           -16777216
        //  4141: ior            
        //  4142: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.background_color:I
        //  4145: goto            4157
        //  4148: aload           23
        //  4150: getfield        org/telegram/tgnet/TLRPC$TL_wallPaper.settings:Lorg/telegram/tgnet/TLRPC$TL_wallPaperSettings;
        //  4153: iconst_m1      
        //  4154: putfield        org/telegram/tgnet/TLRPC$TL_wallPaperSettings.background_color:I
        //  4157: aconst_null    
        //  4158: astore          9
        //  4160: aload           9
        //  4162: astore          11
        //  4164: aload           11
        //  4166: astore          12
        //  4168: aload           12
        //  4170: astore          26
        //  4172: aload           26
        //  4174: astore          13
        //  4176: aload           13
        //  4178: astore          15
        //  4180: aload           15
        //  4182: astore          22
        //  4184: aload           22
        //  4186: astore          17
        //  4188: aload           17
        //  4190: astore          18
        //  4192: aload           18
        //  4194: astore          27
        //  4196: aload           27
        //  4198: astore          19
        //  4200: aload           19
        //  4202: astore          24
        //  4204: goto            5131
        //  4207: aload           11
        //  4209: ldc_w           "login/"
        //  4212: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  4215: ifeq            4277
        //  4218: aload           11
        //  4220: ldc_w           "login/"
        //  4223: ldc_w           ""
        //  4226: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  4229: astore          19
        //  4231: aconst_null    
        //  4232: astore          10
        //  4234: aload           10
        //  4236: astore          11
        //  4238: aload           11
        //  4240: astore          12
        //  4242: aload           12
        //  4244: astore          26
        //  4246: aload           26
        //  4248: astore          13
        //  4250: aload           13
        //  4252: astore          16
        //  4254: aload           16
        //  4256: astore          22
        //  4258: aload           22
        //  4260: astore          17
        //  4262: aload           17
        //  4264: astore          18
        //  4266: aload           18
        //  4268: astore          27
        //  4270: aload           27
        //  4272: astore          9
        //  4274: goto            5115
        //  4277: aload           11
        //  4279: ldc_w           "joinchat/"
        //  4282: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  4285: ifeq            4307
        //  4288: aload           11
        //  4290: ldc_w           "joinchat/"
        //  4293: ldc_w           ""
        //  4296: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  4299: astore          10
        //  4301: aconst_null    
        //  4302: astore          9
        //  4304: goto            5055
        //  4307: aload           11
        //  4309: ldc_w           "addstickers/"
        //  4312: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  4315: ifeq            4341
        //  4318: aload           11
        //  4320: ldc_w           "addstickers/"
        //  4323: ldc_w           ""
        //  4326: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  4329: astore          15
        //  4331: aconst_null    
        //  4332: astore          10
        //  4334: aload           10
        //  4336: astore          11
        //  4338: goto            5063
        //  4341: aload           11
        //  4343: ldc_w           "msg/"
        //  4346: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  4349: ifne            4812
        //  4352: aload           11
        //  4354: ldc_w           "share/"
        //  4357: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  4360: ifeq            4366
        //  4363: goto            4812
        //  4366: aload           11
        //  4368: ldc_w           "confirmphone"
        //  4371: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  4374: ifeq            4431
        //  4377: aload           13
        //  4379: ldc_w           "phone"
        //  4382: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4385: astore          16
        //  4387: aload           13
        //  4389: ldc_w           "hash"
        //  4392: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4395: astore          18
        //  4397: aconst_null    
        //  4398: astore          10
        //  4400: aload           10
        //  4402: astore          15
        //  4404: aload           15
        //  4406: astore          12
        //  4408: aload           12
        //  4410: astore          26
        //  4412: aload           26
        //  4414: astore          13
        //  4416: aload           13
        //  4418: astore          22
        //  4420: aload           22
        //  4422: astore          17
        //  4424: aload           17
        //  4426: astore          9
        //  4428: goto            5095
        //  4431: aload           11
        //  4433: ldc_w           "setlanguage/"
        //  4436: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  4439: ifeq            4493
        //  4442: aload           11
        //  4444: bipush          12
        //  4446: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //  4449: astore          27
        //  4451: aconst_null    
        //  4452: astore          10
        //  4454: aload           10
        //  4456: astore          15
        //  4458: aload           15
        //  4460: astore          12
        //  4462: aload           12
        //  4464: astore          26
        //  4466: aload           26
        //  4468: astore          13
        //  4470: aload           13
        //  4472: astore          16
        //  4474: aload           16
        //  4476: astore          22
        //  4478: aload           22
        //  4480: astore          17
        //  4482: aload           17
        //  4484: astore          18
        //  4486: aload           18
        //  4488: astore          11
        //  4490: goto            5103
        //  4493: aload           11
        //  4495: ldc_w           "c/"
        //  4498: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  4501: ifeq            4641
        //  4504: aload           13
        //  4506: invokevirtual   android/net/Uri.getPathSegments:()Ljava/util/List;
        //  4509: astore          9
        //  4511: aload           9
        //  4513: invokeinterface java/util/List.size:()I
        //  4518: iconst_3       
        //  4519: if_icmpne       4576
        //  4522: aload           9
        //  4524: iconst_1       
        //  4525: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  4530: checkcast       Ljava/lang/String;
        //  4533: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  4536: astore          11
        //  4538: aload           9
        //  4540: iconst_2       
        //  4541: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  4546: checkcast       Ljava/lang/String;
        //  4549: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  4552: astore          9
        //  4554: aload           9
        //  4556: invokevirtual   java/lang/Integer.intValue:()I
        //  4559: ifeq            4576
        //  4562: aload           11
        //  4564: invokevirtual   java/lang/Integer.intValue:()I
        //  4567: ifne            4573
        //  4570: goto            4576
        //  4573: goto            4583
        //  4576: aconst_null    
        //  4577: astore          11
        //  4579: aload           11
        //  4581: astore          9
        //  4583: aload           11
        //  4585: astore          25
        //  4587: aload           9
        //  4589: astore          24
        //  4591: aconst_null    
        //  4592: astore          9
        //  4594: aload           9
        //  4596: astore          11
        //  4598: aload           11
        //  4600: astore          12
        //  4602: aload           12
        //  4604: astore          10
        //  4606: aload           10
        //  4608: astore          13
        //  4610: aload           13
        //  4612: astore          15
        //  4614: aload           15
        //  4616: astore          22
        //  4618: aload           22
        //  4620: astore          17
        //  4622: aload           17
        //  4624: astore          18
        //  4626: aload           18
        //  4628: astore          16
        //  4630: aload           16
        //  4632: astore          19
        //  4634: aload           19
        //  4636: astore          23
        //  4638: goto            5143
        //  4641: aload           11
        //  4643: invokevirtual   java/lang/String.length:()I
        //  4646: iconst_1       
        //  4647: if_icmplt       5048
        //  4650: aload           13
        //  4652: invokevirtual   android/net/Uri.getPathSegments:()Ljava/util/List;
        //  4655: astore          9
        //  4657: aload           9
        //  4659: invokeinterface java/util/List.size:()I
        //  4664: ifle            4733
        //  4667: aload           9
        //  4669: iconst_0       
        //  4670: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  4675: checkcast       Ljava/lang/String;
        //  4678: astore          11
        //  4680: aload           9
        //  4682: invokeinterface java/util/List.size:()I
        //  4687: iconst_1       
        //  4688: if_icmple       4723
        //  4691: aload           9
        //  4693: iconst_1       
        //  4694: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  4699: checkcast       Ljava/lang/String;
        //  4702: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  4705: astore          12
        //  4707: aload           12
        //  4709: astore          9
        //  4711: aload           11
        //  4713: astore          22
        //  4715: aload           12
        //  4717: invokevirtual   java/lang/Integer.intValue:()I
        //  4720: ifne            4740
        //  4723: aconst_null    
        //  4724: astore          9
        //  4726: aload           11
        //  4728: astore          22
        //  4730: goto            4740
        //  4733: aconst_null    
        //  4734: astore          9
        //  4736: aload           9
        //  4738: astore          22
        //  4740: aload           13
        //  4742: ldc_w           "start"
        //  4745: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4748: astore          12
        //  4750: aload           13
        //  4752: ldc_w           "startgroup"
        //  4755: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4758: astore          10
        //  4760: aload           13
        //  4762: ldc_w           "game"
        //  4765: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4768: astore          17
        //  4770: aload           9
        //  4772: astore          24
        //  4774: aconst_null    
        //  4775: astore          9
        //  4777: aload           9
        //  4779: astore          11
        //  4781: aload           11
        //  4783: astore          13
        //  4785: aload           13
        //  4787: astore          15
        //  4789: aload           15
        //  4791: astore          18
        //  4793: aload           18
        //  4795: astore          16
        //  4797: aload           16
        //  4799: astore          19
        //  4801: aload           19
        //  4803: astore          23
        //  4805: aload           23
        //  4807: astore          25
        //  4809: goto            5143
        //  4812: aload           13
        //  4814: ldc_w           "url"
        //  4817: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4820: astore          11
        //  4822: aload           11
        //  4824: ifnonnull       4830
        //  4827: goto            4834
        //  4830: aload           11
        //  4832: astore          9
        //  4834: aload           13
        //  4836: ldc_w           "text"
        //  4839: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4842: ifnull          4934
        //  4845: aload           9
        //  4847: invokevirtual   java/lang/String.length:()I
        //  4850: ifle            4891
        //  4853: new             Ljava/lang/StringBuilder;
        //  4856: dup            
        //  4857: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4860: astore          11
        //  4862: aload           11
        //  4864: aload           9
        //  4866: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4869: pop            
        //  4870: aload           11
        //  4872: ldc_w           "\n"
        //  4875: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4878: pop            
        //  4879: aload           11
        //  4881: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  4884: astore          9
        //  4886: iconst_1       
        //  4887: istore_3       
        //  4888: goto            4893
        //  4891: iconst_0       
        //  4892: istore_3       
        //  4893: new             Ljava/lang/StringBuilder;
        //  4896: dup            
        //  4897: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4900: astore          11
        //  4902: aload           11
        //  4904: aload           9
        //  4906: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4909: pop            
        //  4910: aload           11
        //  4912: aload           13
        //  4914: ldc_w           "text"
        //  4917: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //  4920: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4923: pop            
        //  4924: aload           11
        //  4926: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  4929: astore          9
        //  4931: goto            4936
        //  4934: iconst_0       
        //  4935: istore_3       
        //  4936: aload           9
        //  4938: invokevirtual   java/lang/String.length:()I
        //  4941: sipush          16384
        //  4944: if_icmple       4961
        //  4947: aload           9
        //  4949: iconst_0       
        //  4950: sipush          16384
        //  4953: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  4956: astore          9
        //  4958: goto            4961
        //  4961: aload           9
        //  4963: ldc_w           "\n"
        //  4966: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //  4969: ifeq            4990
        //  4972: aload           9
        //  4974: iconst_0       
        //  4975: aload           9
        //  4977: invokevirtual   java/lang/String.length:()I
        //  4980: iconst_1       
        //  4981: isub           
        //  4982: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  4985: astore          9
        //  4987: goto            4961
        //  4990: aload           9
        //  4992: astore          13
        //  4994: aconst_null    
        //  4995: astore          9
        //  4997: aload           9
        //  4999: astore          11
        //  5001: aload           11
        //  5003: astore          12
        //  5005: aload           12
        //  5007: astore          10
        //  5009: aload           10
        //  5011: astore          15
        //  5013: aload           15
        //  5015: astore          22
        //  5017: aload           22
        //  5019: astore          17
        //  5021: aload           17
        //  5023: astore          18
        //  5025: aload           18
        //  5027: astore          16
        //  5029: aload           16
        //  5031: astore          19
        //  5033: aload           19
        //  5035: astore          23
        //  5037: aload           23
        //  5039: astore          24
        //  5041: aload           24
        //  5043: astore          25
        //  5045: goto            5145
        //  5048: aconst_null    
        //  5049: astore          10
        //  5051: aload           10
        //  5053: astore          9
        //  5055: aload           9
        //  5057: astore          11
        //  5059: aload           9
        //  5061: astore          15
        //  5063: aload           11
        //  5065: astore          26
        //  5067: aload           26
        //  5069: astore          13
        //  5071: aload           13
        //  5073: astore          16
        //  5075: aload           16
        //  5077: astore          22
        //  5079: aload           22
        //  5081: astore          17
        //  5083: aload           17
        //  5085: astore          18
        //  5087: aload           18
        //  5089: astore          9
        //  5091: aload           11
        //  5093: astore          12
        //  5095: aload           9
        //  5097: astore          11
        //  5099: aload           9
        //  5101: astore          27
        //  5103: aload           11
        //  5105: astore          9
        //  5107: aload           11
        //  5109: astore          19
        //  5111: aload           15
        //  5113: astore          11
        //  5115: aload           9
        //  5117: astore          24
        //  5119: aload           9
        //  5121: astore          23
        //  5123: aload           16
        //  5125: astore          15
        //  5127: aload           10
        //  5129: astore          9
        //  5131: aload           24
        //  5133: astore          25
        //  5135: aload           27
        //  5137: astore          16
        //  5139: aload           26
        //  5141: astore          10
        //  5143: iconst_0       
        //  5144: istore_3       
        //  5145: aload           12
        //  5147: astore          26
        //  5149: aload           10
        //  5151: astore          27
        //  5153: aload           24
        //  5155: astore          28
        //  5157: aload           25
        //  5159: astore          24
        //  5161: iconst_0       
        //  5162: istore          5
        //  5164: iconst_0       
        //  5165: istore          20
        //  5167: iconst_0       
        //  5168: istore          14
        //  5170: aload           9
        //  5172: astore          12
        //  5174: aload           16
        //  5176: astore          25
        //  5178: aload           19
        //  5180: astore          9
        //  5182: aconst_null    
        //  5183: astore          19
        //  5185: aload           19
        //  5187: astore          16
        //  5189: aload           11
        //  5191: astore          10
        //  5193: aload           13
        //  5195: astore          11
        //  5197: aload           15
        //  5199: astore          13
        //  5201: aload           22
        //  5203: astore          15
        //  5205: aload           17
        //  5207: astore          22
        //  5209: aload           18
        //  5211: astore          17
        //  5213: aload           19
        //  5215: astore          18
        //  5217: aload           25
        //  5219: astore          19
        //  5221: aload           28
        //  5223: astore          25
        //  5225: goto            5302
        //  5228: aconst_null    
        //  5229: astore          9
        //  5231: aload           9
        //  5233: astore          11
        //  5235: aload           11
        //  5237: astore          12
        //  5239: aload           12
        //  5241: astore          10
        //  5243: aload           10
        //  5245: astore          13
        //  5247: aload           13
        //  5249: astore          15
        //  5251: aload           15
        //  5253: astore          22
        //  5255: aload           22
        //  5257: astore          17
        //  5259: aload           17
        //  5261: astore          18
        //  5263: aload           18
        //  5265: astore          16
        //  5267: aload           16
        //  5269: astore          19
        //  5271: aload           19
        //  5273: astore          23
        //  5275: aload           23
        //  5277: astore          24
        //  5279: aload           24
        //  5281: astore          25
        //  5283: aload           25
        //  5285: astore          26
        //  5287: aload           26
        //  5289: astore          27
        //  5291: iconst_0       
        //  5292: istore_3       
        //  5293: iconst_0       
        //  5294: istore          5
        //  5296: iconst_0       
        //  5297: istore          20
        //  5299: iconst_0       
        //  5300: istore          14
        //  5302: aload           9
        //  5304: ifnonnull       5326
        //  5307: aload_0        
        //  5308: getfield        org/telegram/ui/LaunchActivity.currentAccount:I
        //  5311: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  5314: invokevirtual   org/telegram/messenger/UserConfig.isClientActivated:()Z
        //  5317: ifeq            5323
        //  5320: goto            5326
        //  5323: goto            5808
        //  5326: aload           13
        //  5328: ifnonnull       5766
        //  5331: aload           17
        //  5333: ifnull          5339
        //  5336: goto            5766
        //  5339: aload           15
        //  5341: ifnonnull       5668
        //  5344: aload           12
        //  5346: ifnonnull       5668
        //  5349: aload           10
        //  5351: ifnonnull       5668
        //  5354: aload           11
        //  5356: ifnonnull       5668
        //  5359: aload           22
        //  5361: ifnonnull       5668
        //  5364: aload           18
        //  5366: ifnonnull       5668
        //  5369: aload           16
        //  5371: ifnonnull       5668
        //  5374: aload           19
        //  5376: ifnonnull       5668
        //  5379: aload           9
        //  5381: ifnonnull       5668
        //  5384: aload           23
        //  5386: ifnonnull       5668
        //  5389: aload           24
        //  5391: ifnull          5397
        //  5394: goto            5668
        //  5397: aload_0        
        //  5398: invokevirtual   android/app/Activity.getContentResolver:()Landroid/content/ContentResolver;
        //  5401: aload_1        
        //  5402: invokevirtual   android/content/Intent.getData:()Landroid/net/Uri;
        //  5405: aconst_null    
        //  5406: aconst_null    
        //  5407: aconst_null    
        //  5408: aconst_null    
        //  5409: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //  5412: astore          12
        //  5414: aload           12
        //  5416: ifnull          5614
        //  5419: aload           12
        //  5421: invokeinterface android/database/Cursor.moveToFirst:()Z
        //  5426: ifeq            5614
        //  5429: aload           12
        //  5431: aload           12
        //  5433: ldc_w           "account_name"
        //  5436: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //  5441: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //  5446: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  5449: invokevirtual   java/lang/Integer.intValue:()I
        //  5452: istore          29
        //  5454: iconst_0       
        //  5455: istore          21
        //  5457: iload           21
        //  5459: iconst_3       
        //  5460: if_icmpge       5504
        //  5463: iload           21
        //  5465: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  5468: invokevirtual   org/telegram/messenger/UserConfig.getClientUserId:()I
        //  5471: iload           29
        //  5473: if_icmpne       5498
        //  5476: aload           6
        //  5478: iconst_0       
        //  5479: iload           21
        //  5481: iastore        
        //  5482: aload           6
        //  5484: iconst_0       
        //  5485: iaload         
        //  5486: istore          21
        //  5488: aload_0        
        //  5489: iload           21
        //  5491: iconst_1       
        //  5492: invokevirtual   org/telegram/ui/LaunchActivity.switchToAccount:(IZ)V
        //  5495: goto            5504
        //  5498: iinc            21, 1
        //  5501: goto            5457
        //  5504: aload           12
        //  5506: aload           12
        //  5508: ldc_w           "DATA4"
        //  5511: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //  5516: invokeinterface android/database/Cursor.getInt:(I)I
        //  5521: istore          21
        //  5523: aload           6
        //  5525: iconst_0       
        //  5526: iaload         
        //  5527: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        //  5530: getstatic       org/telegram/messenger/NotificationCenter.closeChats:I
        //  5533: iconst_0       
        //  5534: anewarray       Ljava/lang/Object;
        //  5537: invokevirtual   org/telegram/messenger/NotificationCenter.postNotificationName:(I[Ljava/lang/Object;)V
        //  5540: iload           21
        //  5542: istore          5
        //  5544: goto            5614
        //  5547: astore          9
        //  5549: goto            5559
        //  5552: astore          9
        //  5554: goto            5567
        //  5557: astore          9
        //  5559: aconst_null    
        //  5560: astore          11
        //  5562: goto            5576
        //  5565: astore          9
        //  5567: aload           9
        //  5569: astore          11
        //  5571: aload           11
        //  5573: athrow         
        //  5574: astore          9
        //  5576: aload           12
        //  5578: ifnull          5607
        //  5581: aload           11
        //  5583: ifnull          5596
        //  5586: aload           12
        //  5588: invokeinterface android/database/Cursor.close:()V
        //  5593: goto            5607
        //  5596: iload           5
        //  5598: istore          21
        //  5600: aload           12
        //  5602: invokeinterface android/database/Cursor.close:()V
        //  5607: iload           5
        //  5609: istore          21
        //  5611: aload           9
        //  5613: athrow         
        //  5614: iload           5
        //  5616: istore          21
        //  5618: aload           12
        //  5620: ifnull          5661
        //  5623: iload           5
        //  5625: istore          21
        //  5627: aload           12
        //  5629: invokeinterface android/database/Cursor.close:()V
        //  5634: iload           5
        //  5636: istore          21
        //  5638: goto            5661
        //  5641: astore          9
        //  5643: iload           21
        //  5645: istore          5
        //  5647: goto            5652
        //  5650: astore          9
        //  5652: aload           9
        //  5654: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  5657: iload           5
        //  5659: istore          21
        //  5661: iload           21
        //  5663: istore          5
        //  5665: goto            5820
        //  5668: aload           11
        //  5670: astore          13
        //  5672: aload           11
        //  5674: ifnull          5725
        //  5677: aload           11
        //  5679: astore          13
        //  5681: aload           11
        //  5683: ldc_w           "@"
        //  5686: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  5689: ifeq            5725
        //  5692: new             Ljava/lang/StringBuilder;
        //  5695: dup            
        //  5696: invokespecial   java/lang/StringBuilder.<init>:()V
        //  5699: astore          13
        //  5701: aload           13
        //  5703: ldc_w           " "
        //  5706: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  5709: pop            
        //  5710: aload           13
        //  5712: aload           11
        //  5714: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  5717: pop            
        //  5718: aload           13
        //  5720: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  5723: astore          13
        //  5725: aload_0        
        //  5726: aload           6
        //  5728: iconst_0       
        //  5729: iaload         
        //  5730: aload           15
        //  5732: aload           12
        //  5734: aload           10
        //  5736: aload           26
        //  5738: aload           27
        //  5740: aload           13
        //  5742: iload_3        
        //  5743: aload           25
        //  5745: aload           24
        //  5747: aload           22
        //  5749: aload           18
        //  5751: aload           19
        //  5753: aload           16
        //  5755: aload           9
        //  5757: aload           23
        //  5759: iconst_0       
        //  5760: invokespecial   org/telegram/ui/LaunchActivity.runLinkRequest:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;Ljava/util/HashMap;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/telegram/tgnet/TLRPC$TL_wallPaper;I)V
        //  5763: goto            5808
        //  5766: new             Landroid/os/Bundle;
        //  5769: dup            
        //  5770: invokespecial   android/os/Bundle.<init>:()V
        //  5773: astore          9
        //  5775: aload           9
        //  5777: ldc_w           "phone"
        //  5780: aload           13
        //  5782: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //  5785: aload           9
        //  5787: ldc_w           "hash"
        //  5790: aload           17
        //  5792: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //  5795: new             Lorg/telegram/ui/_$$Lambda$LaunchActivity$N1thb_LLgMOn57u__wgkv5RqrBk;
        //  5798: dup            
        //  5799: aload_0        
        //  5800: aload           9
        //  5802: invokespecial   org/telegram/ui/_$$Lambda$LaunchActivity$N1thb_LLgMOn57u__wgkv5RqrBk.<init>:(Lorg/telegram/ui/LaunchActivity;Landroid/os/Bundle;)V
        //  5805: invokestatic    org/telegram/messenger/AndroidUtilities.runOnUIThread:(Ljava/lang/Runnable;)V
        //  5808: goto            5820
        //  5811: iconst_0       
        //  5812: istore          5
        //  5814: iconst_0       
        //  5815: istore          20
        //  5817: iconst_0       
        //  5818: istore          14
        //  5820: iload           20
        //  5822: istore          21
        //  5824: iload           14
        //  5826: istore          20
        //  5828: aload           6
        //  5830: astore          9
        //  5832: iconst_0       
        //  5833: istore          30
        //  5835: iconst_0       
        //  5836: istore          29
        //  5838: iconst_0       
        //  5839: istore          31
        //  5841: iconst_0       
        //  5842: istore          32
        //  5844: iload           21
        //  5846: istore          14
        //  5848: iload           30
        //  5850: istore          21
        //  5852: goto            6165
        //  5855: aload_1        
        //  5856: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //  5859: ldc_w           "org.telegram.messenger.OPEN_ACCOUNT"
        //  5862: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  5865: ifeq            5880
        //  5868: iconst_0       
        //  5869: istore          21
        //  5871: iconst_0       
        //  5872: istore          29
        //  5874: iconst_1       
        //  5875: istore          31
        //  5877: goto            6149
        //  5880: aload_1        
        //  5881: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //  5884: ldc_w           "new_dialog"
        //  5887: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  5890: ifeq            5908
        //  5893: iconst_0       
        //  5894: istore          21
        //  5896: iconst_0       
        //  5897: istore          29
        //  5899: iconst_0       
        //  5900: istore          31
        //  5902: iconst_1       
        //  5903: istore          32
        //  5905: goto            6152
        //  5908: aload_1        
        //  5909: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //  5912: ldc_w           "com.tmessages.openchat"
        //  5915: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  5918: ifeq            6099
        //  5921: aload_1        
        //  5922: astore          9
        //  5924: aload           9
        //  5926: ldc_w           "chatId"
        //  5929: iconst_0       
        //  5930: invokevirtual   android/content/Intent.getIntExtra:(Ljava/lang/String;I)I
        //  5933: istore          14
        //  5935: aload           9
        //  5937: ldc_w           "userId"
        //  5940: iconst_0       
        //  5941: invokevirtual   android/content/Intent.getIntExtra:(Ljava/lang/String;I)I
        //  5944: istore          5
        //  5946: aload           9
        //  5948: ldc_w           "encId"
        //  5951: iconst_0       
        //  5952: invokevirtual   android/content/Intent.getIntExtra:(Ljava/lang/String;I)I
        //  5955: istore          20
        //  5957: iload           14
        //  5959: ifeq            5991
        //  5962: aload           6
        //  5964: iconst_0       
        //  5965: iaload         
        //  5966: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        //  5969: getstatic       org/telegram/messenger/NotificationCenter.closeChats:I
        //  5972: iconst_0       
        //  5973: anewarray       Ljava/lang/Object;
        //  5976: invokevirtual   org/telegram/messenger/NotificationCenter.postNotificationName:(I[Ljava/lang/Object;)V
        //  5979: iconst_0       
        //  5980: istore          21
        //  5982: iconst_0       
        //  5983: istore          20
        //  5985: iconst_0       
        //  5986: istore          5
        //  5988: goto            6069
        //  5991: aload           6
        //  5993: astore          9
        //  5995: iload           5
        //  5997: ifeq            6029
        //  6000: aload           9
        //  6002: iconst_0       
        //  6003: iaload         
        //  6004: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        //  6007: getstatic       org/telegram/messenger/NotificationCenter.closeChats:I
        //  6010: iconst_0       
        //  6011: anewarray       Ljava/lang/Object;
        //  6014: invokevirtual   org/telegram/messenger/NotificationCenter.postNotificationName:(I[Ljava/lang/Object;)V
        //  6017: iconst_0       
        //  6018: istore          14
        //  6020: iconst_0       
        //  6021: istore          21
        //  6023: iconst_0       
        //  6024: istore          20
        //  6026: goto            6069
        //  6029: iload           20
        //  6031: ifeq            6060
        //  6034: aload           9
        //  6036: iconst_0       
        //  6037: iaload         
        //  6038: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        //  6041: getstatic       org/telegram/messenger/NotificationCenter.closeChats:I
        //  6044: iconst_0       
        //  6045: anewarray       Ljava/lang/Object;
        //  6048: invokevirtual   org/telegram/messenger/NotificationCenter.postNotificationName:(I[Ljava/lang/Object;)V
        //  6051: iconst_0       
        //  6052: istore          14
        //  6054: iconst_0       
        //  6055: istore          21
        //  6057: goto            5985
        //  6060: iconst_0       
        //  6061: istore          14
        //  6063: iconst_1       
        //  6064: istore          21
        //  6066: goto            5982
        //  6069: aload           6
        //  6071: astore          9
        //  6073: iconst_0       
        //  6074: istore          32
        //  6076: iconst_0       
        //  6077: istore          31
        //  6079: iconst_0       
        //  6080: istore          33
        //  6082: iconst_0       
        //  6083: istore          34
        //  6085: iconst_0       
        //  6086: istore          35
        //  6088: iload           20
        //  6090: istore          29
        //  6092: iload           21
        //  6094: istore          30
        //  6096: goto            6195
        //  6099: aload_1        
        //  6100: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //  6103: ldc_w           "com.tmessages.openplayer"
        //  6106: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  6109: ifeq            6118
        //  6112: iconst_1       
        //  6113: istore          21
        //  6115: goto            6143
        //  6118: aload_1        
        //  6119: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //  6122: ldc_w           "org.tmessages.openlocations"
        //  6125: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  6128: ifeq            6140
        //  6131: iconst_0       
        //  6132: istore          21
        //  6134: iconst_1       
        //  6135: istore          29
        //  6137: goto            6146
        //  6140: iconst_0       
        //  6141: istore          21
        //  6143: iconst_0       
        //  6144: istore          29
        //  6146: iconst_0       
        //  6147: istore          31
        //  6149: iconst_0       
        //  6150: istore          32
        //  6152: aload           6
        //  6154: astore          9
        //  6156: iconst_0       
        //  6157: istore          20
        //  6159: iconst_0       
        //  6160: istore          14
        //  6162: iconst_0       
        //  6163: istore          5
        //  6165: iconst_0       
        //  6166: istore          36
        //  6168: iconst_0       
        //  6169: istore          30
        //  6171: iload           32
        //  6173: istore          35
        //  6175: iload           31
        //  6177: istore          34
        //  6179: iload           29
        //  6181: istore          33
        //  6183: iload           21
        //  6185: istore          31
        //  6187: iload           36
        //  6189: istore          29
        //  6191: iload           20
        //  6193: istore          32
        //  6195: aload_0        
        //  6196: astore          11
        //  6198: aload           11
        //  6200: getfield        org/telegram/ui/LaunchActivity.currentAccount:I
        //  6203: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  6206: invokevirtual   org/telegram/messenger/UserConfig.isClientActivated:()Z
        //  6209: ifeq            7393
        //  6212: iload           5
        //  6214: ifeq            6340
        //  6217: new             Landroid/os/Bundle;
        //  6220: dup            
        //  6221: invokespecial   android/os/Bundle.<init>:()V
        //  6224: astore          12
        //  6226: aload           12
        //  6228: ldc_w           "user_id"
        //  6231: iload           5
        //  6233: invokevirtual   android/os/Bundle.putInt:(Ljava/lang/String;I)V
        //  6236: iload           32
        //  6238: ifeq            6251
        //  6241: aload           12
        //  6243: ldc_w           "message_id"
        //  6246: iload           32
        //  6248: invokevirtual   android/os/Bundle.putInt:(Ljava/lang/String;I)V
        //  6251: getstatic       org/telegram/ui/LaunchActivity.mainFragmentsStack:Ljava/util/ArrayList;
        //  6254: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  6257: ifne            6302
        //  6260: aload           9
        //  6262: iconst_0       
        //  6263: iaload         
        //  6264: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //  6267: astore          10
        //  6269: getstatic       org/telegram/ui/LaunchActivity.mainFragmentsStack:Ljava/util/ArrayList;
        //  6272: astore          9
        //  6274: aload           10
        //  6276: aload           12
        //  6278: aload           9
        //  6280: aload           9
        //  6282: invokevirtual   java/util/ArrayList.size:()I
        //  6285: iconst_1       
        //  6286: isub           
        //  6287: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  6290: checkcast       Lorg/telegram/ui/ActionBar/BaseFragment;
        //  6293: invokevirtual   org/telegram/messenger/MessagesController.checkCanOpenChat:(Landroid/os/Bundle;Lorg/telegram/ui/ActionBar/BaseFragment;)Z
        //  6296: ifeq            6335
        //  6299: goto            6302
        //  6302: new             Lorg/telegram/ui/ChatActivity;
        //  6305: dup            
        //  6306: aload           12
        //  6308: invokespecial   org/telegram/ui/ChatActivity.<init>:(Landroid/os/Bundle;)V
        //  6311: astore          9
        //  6313: aload           11
        //  6315: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6318: aload           9
        //  6320: iconst_0       
        //  6321: iconst_1       
        //  6322: iconst_1       
        //  6323: iconst_0       
        //  6324: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.presentFragment:(Lorg/telegram/ui/ActionBar/BaseFragment;ZZZZ)Z
        //  6327: ifeq            6335
        //  6330: iconst_1       
        //  6331: istore_3       
        //  6332: goto            6508
        //  6335: iconst_0       
        //  6336: istore_3       
        //  6337: goto            6508
        //  6340: iload           14
        //  6342: ifeq            6458
        //  6345: new             Landroid/os/Bundle;
        //  6348: dup            
        //  6349: invokespecial   android/os/Bundle.<init>:()V
        //  6352: astore          12
        //  6354: aload           12
        //  6356: ldc_w           "chat_id"
        //  6359: iload           14
        //  6361: invokevirtual   android/os/Bundle.putInt:(Ljava/lang/String;I)V
        //  6364: iload           32
        //  6366: ifeq            6379
        //  6369: aload           12
        //  6371: ldc_w           "message_id"
        //  6374: iload           32
        //  6376: invokevirtual   android/os/Bundle.putInt:(Ljava/lang/String;I)V
        //  6379: getstatic       org/telegram/ui/LaunchActivity.mainFragmentsStack:Ljava/util/ArrayList;
        //  6382: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  6385: ifne            6427
        //  6388: aload           9
        //  6390: iconst_0       
        //  6391: iaload         
        //  6392: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //  6395: astore          10
        //  6397: getstatic       org/telegram/ui/LaunchActivity.mainFragmentsStack:Ljava/util/ArrayList;
        //  6400: astore          9
        //  6402: aload           10
        //  6404: aload           12
        //  6406: aload           9
        //  6408: aload           9
        //  6410: invokevirtual   java/util/ArrayList.size:()I
        //  6413: iconst_1       
        //  6414: isub           
        //  6415: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  6418: checkcast       Lorg/telegram/ui/ActionBar/BaseFragment;
        //  6421: invokevirtual   org/telegram/messenger/MessagesController.checkCanOpenChat:(Landroid/os/Bundle;Lorg/telegram/ui/ActionBar/BaseFragment;)Z
        //  6424: ifeq            6335
        //  6427: new             Lorg/telegram/ui/ChatActivity;
        //  6430: dup            
        //  6431: aload           12
        //  6433: invokespecial   org/telegram/ui/ChatActivity.<init>:(Landroid/os/Bundle;)V
        //  6436: astore          9
        //  6438: aload           11
        //  6440: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6443: aload           9
        //  6445: iconst_0       
        //  6446: iconst_1       
        //  6447: iconst_1       
        //  6448: iconst_0       
        //  6449: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.presentFragment:(Lorg/telegram/ui/ActionBar/BaseFragment;ZZZZ)Z
        //  6452: ifeq            6335
        //  6455: goto            6330
        //  6458: iload           29
        //  6460: ifeq            6511
        //  6463: new             Landroid/os/Bundle;
        //  6466: dup            
        //  6467: invokespecial   android/os/Bundle.<init>:()V
        //  6470: astore          9
        //  6472: aload           9
        //  6474: ldc_w           "enc_id"
        //  6477: iload           29
        //  6479: invokevirtual   android/os/Bundle.putInt:(Ljava/lang/String;I)V
        //  6482: new             Lorg/telegram/ui/ChatActivity;
        //  6485: dup            
        //  6486: aload           9
        //  6488: invokespecial   org/telegram/ui/ChatActivity.<init>:(Landroid/os/Bundle;)V
        //  6491: astore          9
        //  6493: aload           11
        //  6495: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6498: aload           9
        //  6500: iconst_0       
        //  6501: iconst_1       
        //  6502: iconst_1       
        //  6503: iconst_0       
        //  6504: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.presentFragment:(Lorg/telegram/ui/ActionBar/BaseFragment;ZZZZ)Z
        //  6507: istore_3       
        //  6508: goto            7395
        //  6511: iload           30
        //  6513: ifeq            6604
        //  6516: invokestatic    org/telegram/messenger/AndroidUtilities.isTablet:()Z
        //  6519: ifne            6533
        //  6522: aload           11
        //  6524: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6527: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.removeAllFragments:()V
        //  6530: goto            6599
        //  6533: aload           11
        //  6535: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6538: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  6541: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  6544: ifne            6599
        //  6547: aload           11
        //  6549: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6552: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  6555: invokevirtual   java/util/ArrayList.size:()I
        //  6558: iconst_1       
        //  6559: isub           
        //  6560: ifle            6590
        //  6563: aload           11
        //  6565: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6568: astore          9
        //  6570: aload           9
        //  6572: aload           9
        //  6574: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  6577: iconst_0       
        //  6578: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  6581: checkcast       Lorg/telegram/ui/ActionBar/BaseFragment;
        //  6584: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.removeFragmentFromStack:(Lorg/telegram/ui/ActionBar/BaseFragment;)V
        //  6587: goto            6547
        //  6590: aload           11
        //  6592: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6595: iconst_0       
        //  6596: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.closeLastFragment:(Z)V
        //  6599: iconst_0       
        //  6600: istore_2       
        //  6601: goto            6651
        //  6604: iload           31
        //  6606: ifeq            6654
        //  6609: aload           11
        //  6611: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6614: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  6617: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  6620: ifne            6651
        //  6623: aload           11
        //  6625: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6628: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  6631: iconst_0       
        //  6632: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  6635: checkcast       Lorg/telegram/ui/ActionBar/BaseFragment;
        //  6638: new             Lorg/telegram/ui/Components/AudioPlayerAlert;
        //  6641: dup            
        //  6642: aload           11
        //  6644: invokespecial   org/telegram/ui/Components/AudioPlayerAlert.<init>:(Landroid/content/Context;)V
        //  6647: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.showDialog:(Landroid/app/Dialog;)Landroid/app/Dialog;
        //  6650: pop            
        //  6651: goto            7393
        //  6654: iload           33
        //  6656: ifeq            6715
        //  6659: aload           11
        //  6661: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6664: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  6667: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  6670: ifne            6651
        //  6673: aload           11
        //  6675: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6678: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  6681: iconst_0       
        //  6682: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  6685: checkcast       Lorg/telegram/ui/ActionBar/BaseFragment;
        //  6688: new             Lorg/telegram/ui/Components/SharingLocationsAlert;
        //  6691: dup            
        //  6692: aload           11
        //  6694: new             Lorg/telegram/ui/_$$Lambda$LaunchActivity$_qofCdzFwUj12rghSqYUkzh14e4;
        //  6697: dup            
        //  6698: aload           11
        //  6700: aload           9
        //  6702: invokespecial   org/telegram/ui/_$$Lambda$LaunchActivity$_qofCdzFwUj12rghSqYUkzh14e4.<init>:(Lorg/telegram/ui/LaunchActivity;[I)V
        //  6705: invokespecial   org/telegram/ui/Components/SharingLocationsAlert.<init>:(Landroid/content/Context;Lorg/telegram/ui/Components/SharingLocationsAlert$SharingLocationsAlertDelegate;)V
        //  6708: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.showDialog:(Landroid/app/Dialog;)Landroid/app/Dialog;
        //  6711: pop            
        //  6712: goto            6651
        //  6715: aload           11
        //  6717: getfield        org/telegram/ui/LaunchActivity.videoPath:Ljava/lang/String;
        //  6720: ifnonnull       6945
        //  6723: aload           11
        //  6725: getfield        org/telegram/ui/LaunchActivity.photoPathsArray:Ljava/util/ArrayList;
        //  6728: ifnonnull       6945
        //  6731: aload           11
        //  6733: getfield        org/telegram/ui/LaunchActivity.sendingText:Ljava/lang/String;
        //  6736: ifnonnull       6945
        //  6739: aload           11
        //  6741: getfield        org/telegram/ui/LaunchActivity.sendingLocation:Landroid/location/Location;
        //  6744: ifnonnull       6945
        //  6747: aload           11
        //  6749: getfield        org/telegram/ui/LaunchActivity.documentsPathsArray:Ljava/util/ArrayList;
        //  6752: ifnonnull       6945
        //  6755: aload           11
        //  6757: getfield        org/telegram/ui/LaunchActivity.contactsToSend:Ljava/util/ArrayList;
        //  6760: ifnonnull       6945
        //  6763: aload           11
        //  6765: getfield        org/telegram/ui/LaunchActivity.documentsUrisArray:Ljava/util/ArrayList;
        //  6768: ifnull          6774
        //  6771: goto            6945
        //  6774: iload           34
        //  6776: ifeq            6849
        //  6779: aload           11
        //  6781: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6784: new             Lorg/telegram/ui/SettingsActivity;
        //  6787: dup            
        //  6788: invokespecial   org/telegram/ui/SettingsActivity.<init>:()V
        //  6791: iconst_0       
        //  6792: iconst_1       
        //  6793: iconst_1       
        //  6794: iconst_0       
        //  6795: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.presentFragment:(Lorg/telegram/ui/ActionBar/BaseFragment;ZZZZ)Z
        //  6798: pop            
        //  6799: invokestatic    org/telegram/messenger/AndroidUtilities.isTablet:()Z
        //  6802: ifeq            6834
        //  6805: aload           11
        //  6807: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6810: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //  6813: aload           11
        //  6815: getfield        org/telegram/ui/LaunchActivity.rightActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6818: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //  6821: aload           11
        //  6823: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  6826: iconst_0       
        //  6827: iconst_0       
        //  6828: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  6831: goto            6844
        //  6834: aload           11
        //  6836: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  6839: iconst_1       
        //  6840: iconst_0       
        //  6841: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  6844: iconst_1       
        //  6845: istore_3       
        //  6846: goto            7395
        //  6849: iload           35
        //  6851: ifeq            6942
        //  6854: new             Landroid/os/Bundle;
        //  6857: dup            
        //  6858: invokespecial   android/os/Bundle.<init>:()V
        //  6861: astore          9
        //  6863: aload           9
        //  6865: ldc_w           "destroyAfterSelect"
        //  6868: iconst_1       
        //  6869: invokevirtual   android/os/Bundle.putBoolean:(Ljava/lang/String;Z)V
        //  6872: aload           11
        //  6874: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6877: new             Lorg/telegram/ui/ContactsActivity;
        //  6880: dup            
        //  6881: aload           9
        //  6883: invokespecial   org/telegram/ui/ContactsActivity.<init>:(Landroid/os/Bundle;)V
        //  6886: iconst_0       
        //  6887: iconst_1       
        //  6888: iconst_1       
        //  6889: iconst_0       
        //  6890: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.presentFragment:(Lorg/telegram/ui/ActionBar/BaseFragment;ZZZZ)Z
        //  6893: pop            
        //  6894: invokestatic    org/telegram/messenger/AndroidUtilities.isTablet:()Z
        //  6897: ifeq            6929
        //  6900: aload           11
        //  6902: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6905: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //  6908: aload           11
        //  6910: getfield        org/telegram/ui/LaunchActivity.rightActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  6913: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //  6916: aload           11
        //  6918: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  6921: iconst_0       
        //  6922: iconst_0       
        //  6923: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  6926: goto            6844
        //  6929: aload           11
        //  6931: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  6934: iconst_1       
        //  6935: iconst_0       
        //  6936: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  6939: goto            6844
        //  6942: goto            7393
        //  6945: invokestatic    org/telegram/messenger/AndroidUtilities.isTablet:()Z
        //  6948: ifne            6968
        //  6951: aload           9
        //  6953: iconst_0       
        //  6954: iaload         
        //  6955: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        //  6958: getstatic       org/telegram/messenger/NotificationCenter.closeChats:I
        //  6961: iconst_0       
        //  6962: anewarray       Ljava/lang/Object;
        //  6965: invokevirtual   org/telegram/messenger/NotificationCenter.postNotificationName:(I[Ljava/lang/Object;)V
        //  6968: lload           7
        //  6970: lconst_0       
        //  6971: lcmp           
        //  6972: ifne            7360
        //  6975: new             Landroid/os/Bundle;
        //  6978: dup            
        //  6979: invokespecial   android/os/Bundle.<init>:()V
        //  6982: astore          12
        //  6984: aload           12
        //  6986: ldc_w           "onlySelect"
        //  6989: iconst_1       
        //  6990: invokevirtual   android/os/Bundle.putBoolean:(Ljava/lang/String;Z)V
        //  6993: aload           12
        //  6995: ldc_w           "dialogsType"
        //  6998: iconst_3       
        //  6999: invokevirtual   android/os/Bundle.putInt:(Ljava/lang/String;I)V
        //  7002: aload           12
        //  7004: ldc_w           "allowSwitchAccount"
        //  7007: iconst_1       
        //  7008: invokevirtual   android/os/Bundle.putBoolean:(Ljava/lang/String;Z)V
        //  7011: aload           11
        //  7013: getfield        org/telegram/ui/LaunchActivity.contactsToSend:Ljava/util/ArrayList;
        //  7016: astore          9
        //  7018: aload           9
        //  7020: ifnull          7069
        //  7023: aload           9
        //  7025: invokevirtual   java/util/ArrayList.size:()I
        //  7028: iconst_1       
        //  7029: if_icmpeq       7103
        //  7032: aload           12
        //  7034: ldc_w           "selectAlertString"
        //  7037: ldc_w           "SendContactTo"
        //  7040: ldc_w           2131560704
        //  7043: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  7046: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //  7049: aload           12
        //  7051: ldc_w           "selectAlertStringGroup"
        //  7054: ldc_w           "SendContactToGroup"
        //  7057: ldc_w           2131560690
        //  7060: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  7063: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //  7066: goto            7103
        //  7069: aload           12
        //  7071: ldc_w           "selectAlertString"
        //  7074: ldc_w           "SendMessagesTo"
        //  7077: ldc_w           2131560704
        //  7080: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  7083: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //  7086: aload           12
        //  7088: ldc_w           "selectAlertStringGroup"
        //  7091: ldc_w           "SendMessagesToGroup"
        //  7094: ldc_w           2131560705
        //  7097: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  7100: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //  7103: new             Lorg/telegram/ui/DialogsActivity;
        //  7106: dup            
        //  7107: aload           12
        //  7109: invokespecial   org/telegram/ui/DialogsActivity.<init>:(Landroid/os/Bundle;)V
        //  7112: astore          9
        //  7114: aload           9
        //  7116: aload           11
        //  7118: invokevirtual   org/telegram/ui/DialogsActivity.setDelegate:(Lorg/telegram/ui/DialogsActivity$DialogsActivityDelegate;)V
        //  7121: invokestatic    org/telegram/messenger/AndroidUtilities.isTablet:()Z
        //  7124: ifeq            7172
        //  7127: aload           11
        //  7129: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7132: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  7135: invokevirtual   java/util/ArrayList.size:()I
        //  7138: ifle            7220
        //  7141: aload           11
        //  7143: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7146: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  7149: astore          12
        //  7151: aload           12
        //  7153: aload           12
        //  7155: invokevirtual   java/util/ArrayList.size:()I
        //  7158: iconst_1       
        //  7159: isub           
        //  7160: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  7163: instanceof      Lorg/telegram/ui/DialogsActivity;
        //  7166: ifeq            7220
        //  7169: goto            7215
        //  7172: aload           11
        //  7174: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7177: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  7180: invokevirtual   java/util/ArrayList.size:()I
        //  7183: iconst_1       
        //  7184: if_icmple       7220
        //  7187: aload           11
        //  7189: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7192: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  7195: astore          12
        //  7197: aload           12
        //  7199: aload           12
        //  7201: invokevirtual   java/util/ArrayList.size:()I
        //  7204: iconst_1       
        //  7205: isub           
        //  7206: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  7209: instanceof      Lorg/telegram/ui/DialogsActivity;
        //  7212: ifeq            7220
        //  7215: iconst_1       
        //  7216: istore_3       
        //  7217: goto            7222
        //  7220: iconst_0       
        //  7221: istore_3       
        //  7222: aload           11
        //  7224: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7227: aload           9
        //  7229: iload_3        
        //  7230: iconst_1       
        //  7231: iconst_1       
        //  7232: iconst_0       
        //  7233: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.presentFragment:(Lorg/telegram/ui/ActionBar/BaseFragment;ZZZZ)Z
        //  7236: pop            
        //  7237: invokestatic    org/telegram/ui/SecretMediaViewer.hasInstance:()Z
        //  7240: ifeq            7263
        //  7243: invokestatic    org/telegram/ui/SecretMediaViewer.getInstance:()Lorg/telegram/ui/SecretMediaViewer;
        //  7246: invokevirtual   org/telegram/ui/SecretMediaViewer.isVisible:()Z
        //  7249: ifeq            7263
        //  7252: invokestatic    org/telegram/ui/SecretMediaViewer.getInstance:()Lorg/telegram/ui/SecretMediaViewer;
        //  7255: iconst_0       
        //  7256: iconst_0       
        //  7257: invokevirtual   org/telegram/ui/SecretMediaViewer.closePhoto:(ZZ)V
        //  7260: goto            7312
        //  7263: invokestatic    org/telegram/ui/PhotoViewer.hasInstance:()Z
        //  7266: ifeq            7289
        //  7269: invokestatic    org/telegram/ui/PhotoViewer.getInstance:()Lorg/telegram/ui/PhotoViewer;
        //  7272: invokevirtual   org/telegram/ui/PhotoViewer.isVisible:()Z
        //  7275: ifeq            7289
        //  7278: invokestatic    org/telegram/ui/PhotoViewer.getInstance:()Lorg/telegram/ui/PhotoViewer;
        //  7281: iconst_0       
        //  7282: iconst_1       
        //  7283: invokevirtual   org/telegram/ui/PhotoViewer.closePhoto:(ZZ)V
        //  7286: goto            7312
        //  7289: invokestatic    org/telegram/ui/ArticleViewer.hasInstance:()Z
        //  7292: ifeq            7312
        //  7295: invokestatic    org/telegram/ui/ArticleViewer.getInstance:()Lorg/telegram/ui/ArticleViewer;
        //  7298: invokevirtual   org/telegram/ui/ArticleViewer.isVisible:()Z
        //  7301: ifeq            7312
        //  7304: invokestatic    org/telegram/ui/ArticleViewer.getInstance:()Lorg/telegram/ui/ArticleViewer;
        //  7307: iconst_0       
        //  7308: iconst_1       
        //  7309: invokevirtual   org/telegram/ui/ArticleViewer.close:(ZZ)V
        //  7312: aload           11
        //  7314: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  7317: iconst_0       
        //  7318: iconst_0       
        //  7319: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  7322: invokestatic    org/telegram/messenger/AndroidUtilities.isTablet:()Z
        //  7325: ifeq            7347
        //  7328: aload           11
        //  7330: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7333: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //  7336: aload           11
        //  7338: getfield        org/telegram/ui/LaunchActivity.rightActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7341: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //  7344: goto            6844
        //  7347: aload           11
        //  7349: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  7352: iconst_1       
        //  7353: iconst_0       
        //  7354: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  7357: goto            6844
        //  7360: new             Ljava/util/ArrayList;
        //  7363: dup            
        //  7364: invokespecial   java/util/ArrayList.<init>:()V
        //  7367: astore          9
        //  7369: aload           9
        //  7371: lload           7
        //  7373: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  7376: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7379: pop            
        //  7380: aload           11
        //  7382: aconst_null    
        //  7383: aload           9
        //  7385: aconst_null    
        //  7386: iconst_0       
        //  7387: invokevirtual   org/telegram/ui/LaunchActivity.didSelectDialogs:(Lorg/telegram/ui/DialogsActivity;Ljava/util/ArrayList;Ljava/lang/CharSequence;Z)V
        //  7390: goto            7393
        //  7393: iconst_0       
        //  7394: istore_3       
        //  7395: iload_3        
        //  7396: ifne            7652
        //  7399: iload_2        
        //  7400: ifne            7652
        //  7403: invokestatic    org/telegram/messenger/AndroidUtilities.isTablet:()Z
        //  7406: ifeq            7524
        //  7409: aload           11
        //  7411: getfield        org/telegram/ui/LaunchActivity.currentAccount:I
        //  7414: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  7417: invokevirtual   org/telegram/messenger/UserConfig.isClientActivated:()Z
        //  7420: ifne            7466
        //  7423: aload           11
        //  7425: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7428: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  7431: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  7434: ifeq            7622
        //  7437: aload           11
        //  7439: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7442: new             Lorg/telegram/ui/LoginActivity;
        //  7445: dup            
        //  7446: invokespecial   org/telegram/ui/LoginActivity.<init>:()V
        //  7449: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.addFragmentToStack:(Lorg/telegram/ui/ActionBar/BaseFragment;)Z
        //  7452: pop            
        //  7453: aload           11
        //  7455: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  7458: iconst_0       
        //  7459: iconst_0       
        //  7460: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  7463: goto            7622
        //  7466: aload           11
        //  7468: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7471: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  7474: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  7477: ifeq            7622
        //  7480: new             Lorg/telegram/ui/DialogsActivity;
        //  7483: dup            
        //  7484: aconst_null    
        //  7485: invokespecial   org/telegram/ui/DialogsActivity.<init>:(Landroid/os/Bundle;)V
        //  7488: astore          9
        //  7490: aload           9
        //  7492: aload           11
        //  7494: getfield        org/telegram/ui/LaunchActivity.sideMenu:Lorg/telegram/ui/Components/RecyclerListView;
        //  7497: invokevirtual   org/telegram/ui/DialogsActivity.setSideMenu:(Landroidx/recyclerview/widget/RecyclerView;)V
        //  7500: aload           11
        //  7502: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7505: aload           9
        //  7507: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.addFragmentToStack:(Lorg/telegram/ui/ActionBar/BaseFragment;)Z
        //  7510: pop            
        //  7511: aload           11
        //  7513: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  7516: iconst_1       
        //  7517: iconst_0       
        //  7518: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  7521: goto            7622
        //  7524: aload           11
        //  7526: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7529: getfield        org/telegram/ui/ActionBar/ActionBarLayout.fragmentsStack:Ljava/util/ArrayList;
        //  7532: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  7535: ifeq            7622
        //  7538: aload           11
        //  7540: getfield        org/telegram/ui/LaunchActivity.currentAccount:I
        //  7543: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  7546: invokevirtual   org/telegram/messenger/UserConfig.isClientActivated:()Z
        //  7549: ifne            7581
        //  7552: aload           11
        //  7554: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7557: new             Lorg/telegram/ui/LoginActivity;
        //  7560: dup            
        //  7561: invokespecial   org/telegram/ui/LoginActivity.<init>:()V
        //  7564: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.addFragmentToStack:(Lorg/telegram/ui/ActionBar/BaseFragment;)Z
        //  7567: pop            
        //  7568: aload           11
        //  7570: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  7573: iconst_0       
        //  7574: iconst_0       
        //  7575: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  7578: goto            7622
        //  7581: new             Lorg/telegram/ui/DialogsActivity;
        //  7584: dup            
        //  7585: aconst_null    
        //  7586: invokespecial   org/telegram/ui/DialogsActivity.<init>:(Landroid/os/Bundle;)V
        //  7589: astore          9
        //  7591: aload           9
        //  7593: aload           11
        //  7595: getfield        org/telegram/ui/LaunchActivity.sideMenu:Lorg/telegram/ui/Components/RecyclerListView;
        //  7598: invokevirtual   org/telegram/ui/DialogsActivity.setSideMenu:(Landroidx/recyclerview/widget/RecyclerView;)V
        //  7601: aload           11
        //  7603: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7606: aload           9
        //  7608: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.addFragmentToStack:(Lorg/telegram/ui/ActionBar/BaseFragment;)Z
        //  7611: pop            
        //  7612: aload           11
        //  7614: getfield        org/telegram/ui/LaunchActivity.drawerLayoutContainer:Lorg/telegram/ui/ActionBar/DrawerLayoutContainer;
        //  7617: iconst_1       
        //  7618: iconst_0       
        //  7619: invokevirtual   org/telegram/ui/ActionBar/DrawerLayoutContainer.setAllowOpenDrawer:(ZZ)V
        //  7622: aload           11
        //  7624: getfield        org/telegram/ui/LaunchActivity.actionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7627: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //  7630: invokestatic    org/telegram/messenger/AndroidUtilities.isTablet:()Z
        //  7633: ifeq            7652
        //  7636: aload           11
        //  7638: getfield        org/telegram/ui/LaunchActivity.layersActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7641: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //  7644: aload           11
        //  7646: getfield        org/telegram/ui/LaunchActivity.rightActionBarLayout:Lorg/telegram/ui/ActionBar/ActionBarLayout;
        //  7649: invokevirtual   org/telegram/ui/ActionBar/ActionBarLayout.showLastFragment:()V
        //  7652: aload_1        
        //  7653: aconst_null    
        //  7654: invokevirtual   android/content/Intent.setAction:(Ljava/lang/String;)Landroid/content/Intent;
        //  7657: pop            
        //  7658: iload_3        
        //  7659: ireturn        
        //  7660: astore          11
        //  7662: goto            2434
        //  7665: astore          9
        //  7667: goto            2455
        //  7670: astore          9
        //  7672: goto            3057
        //  7675: astore          9
        //  7677: goto            3230
        //  7680: astore          9
        //  7682: goto            3947
        //  7685: astore          9
        //  7687: goto            4157
        //  7690: astore          11
        //  7692: goto            5607
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  334    349    379    386    Ljava/lang/Exception;
        //  354    376    379    386    Ljava/lang/Exception;
        //  1110   1125   1605   1612   Ljava/lang/Exception;
        //  1137   1159   1605   1612   Ljava/lang/Exception;
        //  1163   1181   1605   1612   Ljava/lang/Exception;
        //  1181   1188   1605   1612   Ljava/lang/Exception;
        //  1201   1217   1605   1612   Ljava/lang/Exception;
        //  1236   1244   1605   1612   Ljava/lang/Exception;
        //  1257   1268   1605   1612   Ljava/lang/Exception;
        //  1271   1293   1605   1612   Ljava/lang/Exception;
        //  1297   1315   1605   1612   Ljava/lang/Exception;
        //  1315   1345   1605   1612   Ljava/lang/Exception;
        //  1345   1372   1605   1612   Ljava/lang/Exception;
        //  1381   1403   1605   1612   Ljava/lang/Exception;
        //  1407   1425   1605   1612   Ljava/lang/Exception;
        //  1425   1446   1605   1612   Ljava/lang/Exception;
        //  1468   1492   1605   1612   Ljava/lang/Exception;
        //  1492   1531   1605   1612   Ljava/lang/Exception;
        //  1531   1551   1605   1612   Ljava/lang/Exception;
        //  1554   1577   1605   1612   Ljava/lang/Exception;
        //  1577   1593   1605   1612   Ljava/lang/Exception;
        //  2406   2413   7660   7665   Ljava/lang/NumberFormatException;
        //  2424   2431   7660   7665   Ljava/lang/NumberFormatException;
        //  2445   2452   7665   7670   Ljava/lang/NumberFormatException;
        //  3035   3057   7670   7675   Ljava/lang/Exception;
        //  3193   3230   7675   7680   Ljava/lang/Exception;
        //  3925   3947   7680   7685   Ljava/lang/Exception;
        //  4108   4145   7685   7690   Ljava/lang/Exception;
        //  4148   4157   7685   7690   Ljava/lang/Exception;
        //  5397   5414   5650   5652   Ljava/lang/Exception;
        //  5419   5454   5565   5567   Ljava/lang/Throwable;
        //  5419   5454   5557   5559   Any
        //  5463   5476   5565   5567   Ljava/lang/Throwable;
        //  5463   5476   5557   5559   Any
        //  5488   5495   5552   5557   Ljava/lang/Throwable;
        //  5488   5495   5547   5552   Any
        //  5504   5540   5552   5557   Ljava/lang/Throwable;
        //  5504   5540   5547   5552   Any
        //  5571   5574   5574   5576   Any
        //  5586   5593   7690   7695   Ljava/lang/Throwable;
        //  5600   5607   5641   5650   Ljava/lang/Exception;
        //  5611   5614   5641   5650   Ljava/lang/Exception;
        //  5627   5634   5641   5650   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 3414 out-of-bounds for length 3414
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void onFinish() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        final Runnable lockRunnable = this.lockRunnable;
        if (lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(lockRunnable);
            this.lockRunnable = null;
        }
        final int currentAccount = this.currentAccount;
        if (currentAccount != -1) {
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
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
    
    private void onPasscodePause() {
        final Runnable lockRunnable = this.lockRunnable;
        if (lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(lockRunnable);
            this.lockRunnable = null;
        }
        if (SharedConfig.passcodeHash.length() != 0) {
            SharedConfig.lastPauseTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            this.lockRunnable = new Runnable() {
                @Override
                public void run() {
                    if (LaunchActivity.this.lockRunnable == this) {
                        if (AndroidUtilities.needShowPasscode(true)) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("lock app");
                            }
                            LaunchActivity.this.showPasscodeActivity();
                        }
                        else if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("didn't pass lock check");
                        }
                        LaunchActivity.this.lockRunnable = null;
                    }
                }
            };
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, 1000L);
            }
            else {
                final int autoLockIn = SharedConfig.autoLockIn;
                if (autoLockIn != 0) {
                    AndroidUtilities.runOnUIThread(this.lockRunnable, autoLockIn * 1000L + 1000L);
                }
            }
        }
        else {
            SharedConfig.lastPauseTime = 0;
        }
        SharedConfig.saveConfig();
    }
    
    private void onPasscodeResume() {
        final Runnable lockRunnable = this.lockRunnable;
        if (lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(lockRunnable);
            this.lockRunnable = null;
        }
        if (AndroidUtilities.needShowPasscode(true)) {
            this.showPasscodeActivity();
        }
        if (SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            SharedConfig.saveConfig();
        }
    }
    
    private void runLinkRequest(final int n, String username, String s, String short_name, String s2, final String s3, final String s4, final boolean b, final Integer n2, final Integer n3, final String s5, final HashMap<String, String> hashMap, final String lang_code, final String path, final String s6, final TLRPC.TL_wallPaper tl_wallPaper, int intValue) {
        if (intValue == 0 && UserConfig.getActivatedAccountsCount() >= 2 && hashMap != null) {
            AlertsCreator.createAccountSelectDialog(this, (AlertsCreator.AccountSelectDelegate)new _$$Lambda$LaunchActivity$oQA0vgj3X1YdD8d67UmGyFJyjHY(this, n, username, s, short_name, s2, s3, s4, b, n2, n3, s5, hashMap, lang_code, path, s6, tl_wallPaper)).show();
            return;
        }
        final int n4 = 1;
        if (s6 != null) {
            if (NotificationCenter.getGlobalInstance().hasObservers(NotificationCenter.didReceiveSmsCode)) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReceiveSmsCode, s6);
            }
            else {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("OtherLoginCode", 2131560130, s6)));
                builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                this.showAlertDialog(builder);
            }
            return;
        }
        final AlertDialog alertDialog = new AlertDialog((Context)this, 3);
        final Object o = { 0 };
        AlertDialog alertDialog2 = null;
        Label_1046: {
            if (username != null) {
                final TLRPC.TL_contacts_resolveUsername tl_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
                tl_contacts_resolveUsername.username = username;
                o[0] = ConnectionsManager.getInstance(n).sendRequest(tl_contacts_resolveUsername, new _$$Lambda$LaunchActivity$RpMN6VrBiIorLulFIXuvXk8Fkc4(this, alertDialog, s5, n, s3, s2, n2));
                alertDialog2 = alertDialog;
            }
            else if (s != null) {
                if (intValue == 0) {
                    final TLRPC.TL_messages_checkChatInvite tl_messages_checkChatInvite = new TLRPC.TL_messages_checkChatInvite();
                    tl_messages_checkChatInvite.hash = s;
                    o[0] = ConnectionsManager.getInstance(n).sendRequest(tl_messages_checkChatInvite, new _$$Lambda$LaunchActivity$Y1VGiKNTbvASO61ltlphoI7h8RU(this, alertDialog, n, s, username, short_name, s2, s3, s4, b, n2, n3, s5, hashMap, lang_code, path, s6, tl_wallPaper), 2);
                }
                else if (intValue == 1) {
                    final TLRPC.TL_messages_importChatInvite tl_messages_importChatInvite = new TLRPC.TL_messages_importChatInvite();
                    tl_messages_importChatInvite.hash = s;
                    final ConnectionsManager instance = ConnectionsManager.getInstance(n);
                    alertDialog2 = alertDialog;
                    instance.sendRequest(tl_messages_importChatInvite, new _$$Lambda$LaunchActivity$Zfo5ch07dnPuGi7d59vdlVijeac(this, n, alertDialog2), 2);
                    break Label_1046;
                }
                alertDialog2 = alertDialog;
            }
            else {
                s2 = (String)o;
                if (short_name != null) {
                    if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                        final TLRPC.TL_inputStickerSetShortName tl_inputStickerSetShortName = new TLRPC.TL_inputStickerSetShortName();
                        tl_inputStickerSetShortName.short_name = short_name;
                        final ArrayList<BaseFragment> mainFragmentsStack = LaunchActivity.mainFragmentsStack;
                        final BaseFragment baseFragment = mainFragmentsStack.get(mainFragmentsStack.size() - 1);
                        baseFragment.showDialog(new StickersAlert((Context)this, baseFragment, tl_inputStickerSetShortName, null, null));
                    }
                    return;
                }
                if (s4 != null) {
                    final Bundle bundle = new Bundle();
                    bundle.putBoolean("onlySelect", true);
                    final DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                    dialogsActivity.setDelegate((DialogsActivity.DialogsActivityDelegate)new _$$Lambda$LaunchActivity$RTwp0QB0Dkt4irgon9YXmRC5sCk(this, b, n, s4));
                    this.presentFragment(dialogsActivity, false, true);
                    alertDialog2 = alertDialog;
                }
                else if (hashMap != null) {
                    intValue = Utilities.parseInt(hashMap.get("bot_id"));
                    if (intValue == 0) {
                        return;
                    }
                    username = hashMap.get("payload");
                    short_name = hashMap.get("nonce");
                    s = hashMap.get("callback_url");
                    final TLRPC.TL_account_getAuthorizationForm tl_account_getAuthorizationForm = new TLRPC.TL_account_getAuthorizationForm();
                    tl_account_getAuthorizationForm.bot_id = intValue;
                    tl_account_getAuthorizationForm.scope = hashMap.get("scope");
                    tl_account_getAuthorizationForm.public_key = hashMap.get("public_key");
                    s2[0] = ConnectionsManager.getInstance(n).sendRequest(tl_account_getAuthorizationForm, new _$$Lambda$LaunchActivity$Art3qkoRK5cBs4Y4xN6A8ukcezg(this, (int[])(Object)s2, n, alertDialog, tl_account_getAuthorizationForm, username, short_name, s));
                    alertDialog2 = alertDialog;
                }
                else if (path != null) {
                    final TLRPC.TL_help_getDeepLinkInfo tl_help_getDeepLinkInfo = new TLRPC.TL_help_getDeepLinkInfo();
                    tl_help_getDeepLinkInfo.path = path;
                    s2[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_help_getDeepLinkInfo, new _$$Lambda$LaunchActivity$qH1w5GBYl7awDhwaFS3FGWwAWCs(this, alertDialog));
                    alertDialog2 = alertDialog;
                }
                else if (lang_code != null) {
                    final TLRPC.TL_langpack_getLanguage tl_langpack_getLanguage = new TLRPC.TL_langpack_getLanguage();
                    tl_langpack_getLanguage.lang_code = lang_code;
                    tl_langpack_getLanguage.lang_pack = "android";
                    s2[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_langpack_getLanguage, new _$$Lambda$LaunchActivity$BzXeKQ_qpCSdfuqJJ22744bcPM0(this, alertDialog));
                    alertDialog2 = alertDialog;
                }
                else if (tl_wallPaper != null) {
                    Label_0848: {
                        if (TextUtils.isEmpty((CharSequence)tl_wallPaper.slug)) {
                            try {
                                AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$zROn3pMkFlHNUe2IsaYIfrrTR3o(this, new WallpaperActivity(new WallpapersListActivity.ColorWallpaper(-100L, tl_wallPaper.settings.background_color), null)));
                                intValue = n4;
                                break Label_0848;
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                            }
                        }
                        intValue = 0;
                    }
                    alertDialog2 = alertDialog;
                    if (intValue == 0) {
                        final TLRPC.TL_account_getWallPaper tl_account_getWallPaper = new TLRPC.TL_account_getWallPaper();
                        final TLRPC.TL_inputWallPaperSlug wallpaper = new TLRPC.TL_inputWallPaperSlug();
                        wallpaper.slug = tl_wallPaper.slug;
                        tl_account_getWallPaper.wallpaper = wallpaper;
                        s2[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_getWallPaper, new _$$Lambda$LaunchActivity$KNg5IaCFYbOpTWjqZaxL1vwTVgk(this, alertDialog, tl_wallPaper));
                        alertDialog2 = alertDialog;
                    }
                }
                else {
                    alertDialog2 = alertDialog;
                    if (n3 != null) {
                        alertDialog2 = alertDialog;
                        if (n2 != null) {
                            final Bundle bundle2 = new Bundle();
                            bundle2.putInt("chat_id", (int)n3);
                            bundle2.putInt("message_id", (int)n2);
                            BaseFragment baseFragment2;
                            if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                                final ArrayList<BaseFragment> mainFragmentsStack2 = LaunchActivity.mainFragmentsStack;
                                baseFragment2 = mainFragmentsStack2.get(mainFragmentsStack2.size() - 1);
                            }
                            else {
                                baseFragment2 = null;
                            }
                            if (baseFragment2 != null) {
                                alertDialog2 = alertDialog;
                                if (!MessagesController.getInstance(n).checkCanOpenChat(bundle2, baseFragment2)) {
                                    break Label_1046;
                                }
                            }
                            AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$PRISru_yyJ8MquHeYUjmNXD8lxM(this, bundle2, n3, (int[])(Object)s2, alertDialog, baseFragment2, n));
                            alertDialog2 = alertDialog;
                        }
                    }
                }
            }
        }
        if (!o[0]) {
            return;
        }
        alertDialog2.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$LaunchActivity$ktWr8n6LYHE5Dk0KQpsKpoFM5CM(n, (int[])o));
        try {
            alertDialog2.show();
        }
        catch (Exception ex2) {}
    }
    
    private void showLanguageAlert(final boolean b) {
        try {
            if (!this.loadingLocaleDialog) {
                if (!ApplicationLoader.mainInterfacePaused) {
                    final String string = MessagesController.getGlobalMainSettings().getString("language_showed2", "");
                    final String suggestedLangCode = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
                    if (!b && string.equals(suggestedLangCode)) {
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("alert already showed for ");
                            sb.append(string);
                            FileLog.d(sb.toString());
                        }
                        return;
                    }
                    final LocaleController.LocaleInfo[] array = new LocaleController.LocaleInfo[2];
                    String s;
                    if (suggestedLangCode.contains("-")) {
                        s = suggestedLangCode.split("-")[0];
                    }
                    else {
                        s = suggestedLangCode;
                    }
                    String anObject;
                    if ("in".equals(s)) {
                        anObject = "id";
                    }
                    else if ("iw".equals(s)) {
                        anObject = "he";
                    }
                    else if ("jw".equals(s)) {
                        anObject = "jv";
                    }
                    else {
                        anObject = null;
                    }
                    for (int i = 0; i < LocaleController.getInstance().languages.size(); ++i) {
                        final LocaleController.LocaleInfo localeInfo = LocaleController.getInstance().languages.get(i);
                        if (localeInfo.shortName.equals("en")) {
                            array[0] = localeInfo;
                        }
                        if (localeInfo.shortName.replace("_", "-").equals(suggestedLangCode) || localeInfo.shortName.equals(s) || localeInfo.shortName.equals(anObject)) {
                            array[1] = localeInfo;
                        }
                        if (array[0] != null && array[1] != null) {
                            break;
                        }
                    }
                    if (array[0] != null && array[1] != null) {
                        if (array[0] != array[1]) {
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("show lang alert for ");
                                sb2.append(array[0].getKey());
                                sb2.append(" and ");
                                sb2.append(array[1].getKey());
                                FileLog.d(sb2.toString());
                            }
                            this.systemLocaleStrings = null;
                            this.englishLocaleStrings = null;
                            this.loadingLocaleDialog = true;
                            final TLRPC.TL_langpack_getStrings tl_langpack_getStrings = new TLRPC.TL_langpack_getStrings();
                            tl_langpack_getStrings.lang_code = array[1].getLangCode();
                            tl_langpack_getStrings.keys.add("English");
                            tl_langpack_getStrings.keys.add("ChooseYourLanguage");
                            tl_langpack_getStrings.keys.add("ChooseYourLanguageOther");
                            tl_langpack_getStrings.keys.add("ChangeLanguageLater");
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_langpack_getStrings, new _$$Lambda$LaunchActivity$zUSUlC8LSla6kKMHoMSEb1G2TkE(this, array, suggestedLangCode), 8);
                            final TLRPC.TL_langpack_getStrings tl_langpack_getStrings2 = new TLRPC.TL_langpack_getStrings();
                            tl_langpack_getStrings2.lang_code = array[0].getLangCode();
                            tl_langpack_getStrings2.keys.add("English");
                            tl_langpack_getStrings2.keys.add("ChooseYourLanguage");
                            tl_langpack_getStrings2.keys.add("ChooseYourLanguageOther");
                            tl_langpack_getStrings2.keys.add("ChangeLanguageLater");
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_langpack_getStrings2, new _$$Lambda$LaunchActivity$3j0ynD_14Ne6162eLDw5z0libqA(this, array, suggestedLangCode), 8);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void showLanguageAlertInternal(LocaleController.LocaleInfo localeInfo, final LocaleController.LocaleInfo localeInfo2, final String s) {
        try {
            this.loadingLocaleDialog = false;
            final boolean b = localeInfo.builtIn || LocaleController.getInstance().isCurrentLocalLocale();
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
            builder.setTitle(this.getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguage", 2131559098));
            builder.setSubtitle(this.getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", 2131559098));
            final LinearLayout view = new LinearLayout((Context)this);
            view.setOrientation(1);
            final LanguageCell[] array = new LanguageCell[2];
            final LocaleController.LocaleInfo[] array2 = { null };
            final LocaleController.LocaleInfo[] array3 = new LocaleController.LocaleInfo[2];
            final String stringForLanguageAlert = this.getStringForLanguageAlert(this.systemLocaleStrings, "English", 2131559365);
            LocaleController.LocaleInfo localeInfo3;
            if (b) {
                localeInfo3 = localeInfo;
            }
            else {
                localeInfo3 = localeInfo2;
            }
            array3[0] = localeInfo3;
            LocaleController.LocaleInfo localeInfo4;
            if (b) {
                localeInfo4 = localeInfo2;
            }
            else {
                localeInfo4 = localeInfo;
            }
            array3[1] = localeInfo4;
            if (!b) {
                localeInfo = localeInfo2;
            }
            array2[0] = localeInfo;
            for (int i = 0; i < 2; ++i) {
                array[i] = new LanguageCell((Context)this, true);
                final LanguageCell languageCell = array[i];
                final LocaleController.LocaleInfo localeInfo5 = array3[i];
                String s2;
                if (array3[i] == localeInfo2) {
                    s2 = stringForLanguageAlert;
                }
                else {
                    s2 = null;
                }
                languageCell.setLanguage(localeInfo5, s2, true);
                array[i].setTag((Object)i);
                array[i].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
                array[i].setLanguageSelected(i == 0);
                view.addView((View)array[i], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                array[i].setOnClickListener((View$OnClickListener)new _$$Lambda$LaunchActivity$xW_6R4h9aa4jorowKHJF_yJiIQM(array2, array));
            }
            final LanguageCell languageCell2 = new LanguageCell((Context)this, true);
            languageCell2.setValue(this.getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguageOther", 2131559099), this.getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", 2131559099));
            languageCell2.setOnClickListener((View$OnClickListener)new _$$Lambda$LaunchActivity$H9lUoWlciprQBKCCa3uBhilp70I(this));
            view.addView((View)languageCell2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
            builder.setView((View)view);
            builder.setNegativeButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$LaunchActivity$__OYvDrFInFakn0WORCpq62kH08(this, array2));
            this.localeDialog = this.showAlertDialog(builder);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", s).commit();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void showPasscodeActivity() {
        if (this.passcodeView == null) {
            return;
        }
        SharedConfig.appLocked = true;
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
        }
        else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
        }
        else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        this.passcodeView.onShow();
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.passcodeView.setDelegate((PasscodeView.PasscodeViewDelegate)new _$$Lambda$LaunchActivity$V1zg09F7Jz3e7nYXLjhnV60E_O8(this));
        this.actionBarLayout.setVisibility(4);
        if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
                this.layersActionBarLayout.setVisibility(4);
            }
            this.rightActionBarLayout.setVisibility(4);
        }
    }
    
    private void showTosActivity(final int n, final TLRPC.TL_help_termsOfService unacceptedTermsOfService) {
        if (this.termsOfServiceView == null) {
            this.termsOfServiceView = new TermsOfServiceView((Context)this);
            this.drawerLayoutContainer.addView((View)this.termsOfServiceView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            this.termsOfServiceView.setDelegate((TermsOfServiceView.TermsOfServiceViewDelegate)new TermsOfServiceView.TermsOfServiceViewDelegate() {
                @Override
                public void onAcceptTerms(final int n) {
                    UserConfig.getInstance(n).unacceptedTermsOfService = null;
                    UserConfig.getInstance(n).saveConfig(false);
                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    LaunchActivity.this.termsOfServiceView.setVisibility(8);
                }
                
                @Override
                public void onDeclineTerms(final int n) {
                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    LaunchActivity.this.termsOfServiceView.setVisibility(8);
                }
            });
        }
        final TLRPC.TL_help_termsOfService unacceptedTermsOfService2 = UserConfig.getInstance(n).unacceptedTermsOfService;
        if (unacceptedTermsOfService2 != unacceptedTermsOfService && (unacceptedTermsOfService2 == null || !unacceptedTermsOfService2.id.data.equals(unacceptedTermsOfService.id.data))) {
            UserConfig.getInstance(n).unacceptedTermsOfService = unacceptedTermsOfService;
            UserConfig.getInstance(n).saveConfig(false);
        }
        this.termsOfServiceView.show(n, unacceptedTermsOfService);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    }
    
    private void showUpdateActivity(final int n, final TLRPC.TL_help_appUpdate tl_help_appUpdate) {
        if (this.blockingUpdateView == null) {
            this.blockingUpdateView = new BlockingUpdateView(this) {
                @Override
                public void setVisibility(final int visibility) {
                    super.setVisibility(visibility);
                    if (visibility == 8) {
                        LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    }
                }
            };
            this.drawerLayoutContainer.addView((View)this.blockingUpdateView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        }
        this.blockingUpdateView.show(n, tl_help_appUpdate);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    }
    
    private void switchToAvailableAccountOrLogout() {
        while (true) {
            for (int i = 0; i < 3; ++i) {
                if (UserConfig.getInstance(i).isClientActivated()) {
                    final TermsOfServiceView termsOfServiceView = this.termsOfServiceView;
                    if (termsOfServiceView != null) {
                        termsOfServiceView.setVisibility(8);
                    }
                    if (i != -1) {
                        this.switchToAccount(i, true);
                    }
                    else {
                        final DrawerLayoutAdapter drawerLayoutAdapter = this.drawerLayoutAdapter;
                        if (drawerLayoutAdapter != null) {
                            drawerLayoutAdapter.notifyDataSetChanged();
                        }
                        final Iterator<BaseFragment> iterator = this.actionBarLayout.fragmentsStack.iterator();
                        while (iterator.hasNext()) {
                            iterator.next().onFragmentDestroy();
                        }
                        this.actionBarLayout.fragmentsStack.clear();
                        if (AndroidUtilities.isTablet()) {
                            final Iterator<BaseFragment> iterator2 = this.layersActionBarLayout.fragmentsStack.iterator();
                            while (iterator2.hasNext()) {
                                iterator2.next().onFragmentDestroy();
                            }
                            this.layersActionBarLayout.fragmentsStack.clear();
                            final Iterator<BaseFragment> iterator3 = this.rightActionBarLayout.fragmentsStack.iterator();
                            while (iterator3.hasNext()) {
                                iterator3.next().onFragmentDestroy();
                            }
                            this.rightActionBarLayout.fragmentsStack.clear();
                        }
                        this.startActivity(new Intent((Context)this, (Class)IntroActivity.class));
                        this.onFinish();
                        this.finish();
                    }
                    return;
                }
            }
            int i = -1;
            continue;
        }
    }
    
    private void updateCurrentConnectionState(int n) {
        if (this.actionBarLayout == null) {
            return;
        }
        n = 0;
        this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        final int currentConnectionState = this.currentConnectionState;
        Runnable runnable = null;
        String s;
        if (currentConnectionState == 2) {
            n = 2131561102;
            s = "WaitingForNetwork";
        }
        else if (currentConnectionState == 5) {
            n = 2131560962;
            s = "Updating";
        }
        else if (currentConnectionState == 4) {
            n = 2131559139;
            s = "ConnectingToProxy";
        }
        else if (currentConnectionState == 1) {
            n = 2131559137;
            s = "Connecting";
        }
        else {
            s = null;
        }
        final int currentConnectionState2 = this.currentConnectionState;
        if (currentConnectionState2 == 1 || currentConnectionState2 == 4) {
            runnable = new _$$Lambda$LaunchActivity$zB6R9P65ZPbtKEyhZOKuv4q935o(this);
        }
        this.actionBarLayout.setTitleOverlayText(s, n, runnable);
    }
    
    public void checkAppUpdate(final boolean b) {
        if ((!b && BuildVars.DEBUG_VERSION) || (!b && !BuildVars.CHECK_UPDATES)) {
            return;
        }
        if (!b && Math.abs(System.currentTimeMillis() - UserConfig.getInstance(0).lastUpdateCheckTime) < 86400000L) {
            return;
        }
        final TLRPC.TL_help_getAppUpdate tl_help_getAppUpdate = new TLRPC.TL_help_getAppUpdate();
        while (true) {
            try {
                tl_help_getAppUpdate.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
                if (tl_help_getAppUpdate.source == null) {
                    tl_help_getAppUpdate.source = "";
                }
                final int currentAccount = this.currentAccount;
                ConnectionsManager.getInstance(currentAccount).sendRequest(tl_help_getAppUpdate, new _$$Lambda$LaunchActivity$1W6W_t_pAiSBx5i38jWX5eUMkro(this, currentAccount));
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public void didReceivedNotification(int connectionState, int childCount, final Object... array) {
        if (connectionState == NotificationCenter.appDidLogout) {
            this.switchToAvailableAccountOrLogout();
            return;
        }
        final int closeOtherAppActivities = NotificationCenter.closeOtherAppActivities;
        final int n = 0;
        final boolean b = false;
        StringBuilder sb;
        Integer n2;
        AlertDialog.Builder builder;
        ArrayList<BaseFragment> mainFragmentsStack;
        HashMap hashMap;
        AlertDialog.Builder builder2;
        ArrayList<BaseFragment> mainFragmentsStack2;
        RecyclerListView sideMenu;
        View child;
        boolean b2;
        ArrayList<BaseFragment> mainFragmentsStack3;
        ArticleViewer instance;
        ArrayList<BaseFragment> mainFragmentsStack4;
        ActionBarLayout actionBarLayout;
        HashMap hashMap2;
        boolean booleanValue;
        boolean booleanValue2;
        ArrayList<BaseFragment> fragmentsStack;
        BaseFragment baseFragment;
        AlertDialog.Builder builder3;
        AlertDialog create;
        RecyclerListView sideMenu2;
        Theme.ThemeInfo themeInfo;
        boolean booleanValue3;
        Integer n3;
        RecyclerListView sideMenu3;
        View child2;
        Block_51_Outer:Label_1414_Outer:
        while (true) {
            if (connectionState == closeOtherAppActivities) {
                if (array[0] != this) {
                    this.onFinish();
                    this.finish();
                }
                return;
            }
            else if (connectionState == NotificationCenter.didUpdateConnectionState) {
                connectionState = ConnectionsManager.getInstance(childCount).getConnectionState();
                if (this.currentConnectionState != connectionState) {
                    if (BuildVars.LOGS_ENABLED) {
                        sb = new StringBuilder();
                        sb.append("switch to state ");
                        sb.append(connectionState);
                        FileLog.d(sb.toString());
                    }
                    this.currentConnectionState = connectionState;
                    this.updateCurrentConnectionState(childCount);
                }
                return;
            }
            else {
                if (connectionState == NotificationCenter.mainUserInfoChanged) {
                    this.drawerLayoutAdapter.notifyDataSetChanged();
                    return;
                }
                if (connectionState == NotificationCenter.needShowAlert) {
                    n2 = (Integer)array[0];
                    if (n2 == 3 && this.proxyErrorDialog != null) {
                        return;
                    }
                    if (n2 == 4) {
                        this.showTosActivity(childCount, (TLRPC.TL_help_termsOfService)array[1]);
                        return;
                    }
                    builder = new AlertDialog.Builder((Context)this);
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    if (n2 != 2 && n2 != 3) {
                        builder.setNegativeButton(LocaleController.getString("MoreInfo", 2131559883), (DialogInterface$OnClickListener)new _$$Lambda$LaunchActivity$sNXuL3LKkNwiULIaBmkQorXYhkE(childCount));
                    }
                    if (n2 == 5) {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam3", 2131559959));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    }
                    else if (n2 == 0) {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam1", 2131559957));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    }
                    else if (n2 == 1) {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam2", 2131559958));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    }
                    else if (n2 == 2) {
                        builder.setMessage((CharSequence)array[1]);
                        if (((String)array[2]).startsWith("AUTH_KEY_DROP_")) {
                            builder.setPositiveButton(LocaleController.getString("Cancel", 2131558891), null);
                            builder.setNegativeButton(LocaleController.getString("LogOut", 2131559783), (DialogInterface$OnClickListener)new _$$Lambda$LaunchActivity$KI45KCZymdE2FpdMIxAM9_1Ovt8(this));
                        }
                        else {
                            builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                        }
                    }
                    else if (n2 == 3) {
                        builder.setMessage(LocaleController.getString("UseProxyTelegramError", 2131560983));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                        this.proxyErrorDialog = this.showAlertDialog(builder);
                        return;
                    }
                    if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                        mainFragmentsStack = LaunchActivity.mainFragmentsStack;
                        mainFragmentsStack.get(mainFragmentsStack.size() - 1).showDialog(builder.create());
                    }
                    return;
                }
                else if (connectionState == NotificationCenter.wasUnableToFindCurrentLocation) {
                    hashMap = (HashMap)array[0];
                    builder2 = new AlertDialog.Builder((Context)this);
                    builder2.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder2.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    builder2.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", 2131560758), (DialogInterface$OnClickListener)new _$$Lambda$LaunchActivity$g3PH5EQViR8m49F5t2uLHcHps64(this, hashMap, childCount));
                    builder2.setMessage(LocaleController.getString("ShareYouLocationUnable", 2131560757));
                    if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                        mainFragmentsStack2 = LaunchActivity.mainFragmentsStack;
                        mainFragmentsStack2.get(mainFragmentsStack2.size() - 1).showDialog(builder2.create());
                    }
                    return;
                }
                else if (connectionState == NotificationCenter.didSetNewWallpapper) {
                    sideMenu = this.sideMenu;
                    if (sideMenu == null) {
                        return;
                    }
                    child = sideMenu.getChildAt(0);
                    if (child != null) {
                        child.invalidate();
                    }
                    return;
                }
                else if (connectionState == NotificationCenter.didSetPasscode) {
                    if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
                        try {
                            this.getWindow().setFlags(8192, 8192);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        return;
                    }
                    if (!MediaController.getInstance().hasFlagSecureFragment()) {
                        try {
                            this.getWindow().clearFlags(8192);
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                        }
                    }
                    return;
                }
                else {
                    if (connectionState == NotificationCenter.reloadInterface) {
                        b2 = b;
                        if (LaunchActivity.mainFragmentsStack.size() > 1) {
                            mainFragmentsStack3 = LaunchActivity.mainFragmentsStack;
                            b2 = b;
                            if (mainFragmentsStack3.get(mainFragmentsStack3.size() - 1) instanceof SettingsActivity) {
                                b2 = true;
                            }
                        }
                        this.rebuildAllFragments(b2);
                        return;
                    }
                    if (connectionState == NotificationCenter.suggestedLangpack) {
                        this.showLanguageAlert(false);
                        return;
                    }
                    if (connectionState == NotificationCenter.openArticle) {
                        if (LaunchActivity.mainFragmentsStack.isEmpty()) {
                            return;
                        }
                        instance = ArticleViewer.getInstance();
                        mainFragmentsStack4 = LaunchActivity.mainFragmentsStack;
                        instance.setParentActivity(this, mainFragmentsStack4.get(mainFragmentsStack4.size() - 1));
                        ArticleViewer.getInstance().open((TLRPC.TL_webPage)array[0], (String)array[1]);
                        return;
                    }
                    else if (connectionState == NotificationCenter.hasNewContactsToImport) {
                        actionBarLayout = this.actionBarLayout;
                        if (actionBarLayout != null && !actionBarLayout.fragmentsStack.isEmpty()) {
                            (int)array[0];
                            hashMap2 = (HashMap)array[1];
                            booleanValue = (boolean)array[2];
                            booleanValue2 = (boolean)array[3];
                            fragmentsStack = this.actionBarLayout.fragmentsStack;
                            baseFragment = fragmentsStack.get(fragmentsStack.size() - 1);
                            builder3 = new AlertDialog.Builder((Context)this);
                            builder3.setTitle(LocaleController.getString("UpdateContactsTitle", 2131560953));
                            builder3.setMessage(LocaleController.getString("UpdateContactsMessage", 2131560952));
                            builder3.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$LaunchActivity$V0ixus5t_k3sjeAzTAOnXbxIAXc(childCount, hashMap2, booleanValue, booleanValue2));
                            builder3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (DialogInterface$OnClickListener)new _$$Lambda$LaunchActivity$lR7vjLvAzG_cCRS6htHMjvKKewI(childCount, hashMap2, booleanValue, booleanValue2));
                            builder3.setOnBackButtonListener((DialogInterface$OnClickListener)new _$$Lambda$LaunchActivity$6f4_bg5ZpiXCOo5ikvs5uNelDMk(childCount, hashMap2, booleanValue, booleanValue2));
                            create = builder3.create();
                            baseFragment.showDialog(create);
                            create.setCanceledOnTouchOutside(false);
                        }
                        return;
                    }
                    else {
                        if (connectionState != NotificationCenter.didSetNewTheme) {
                            break Block_51_Outer;
                        }
                        if (array[0]) {
                            break Label_1296;
                        }
                        sideMenu2 = this.sideMenu;
                        if (sideMenu2 != null) {
                            sideMenu2.setBackgroundColor(Theme.getColor("chats_menuBackground"));
                            this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
                            this.sideMenu.setListSelectorColor(Theme.getColor("listSelectorSDK21"));
                            this.sideMenu.getAdapter().notifyDataSetChanged();
                        }
                        if (Build$VERSION.SDK_INT < 21) {
                            break Label_1296;
                        }
                    }
                }
            }
            try {
                this.setTaskDescription(new ActivityManager$TaskDescription((String)null, (Bitmap)null, Theme.getColor("actionBarDefault") | 0xFF000000));
                this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
                Label_1466: {
                    return;
                }
                // iftrue(Label_1380:, connectionState != NotificationCenter.needSetDayNightTheme)
                // iftrue(Label_1466:, connectionState != NotificationCenter.notificationsCountUpdated)
                // iftrue(Label_1466:, !AndroidUtilities.isTablet())
                // iftrue(Label_1466:, sideMenu3 == null)
            Label_1414:
                while (true) {
                    while (true) {
                        while (true) {
                        Block_50:
                            while (true) {
                                Block_48: {
                                    break Block_48;
                                    this.layersActionBarLayout.animateThemedValues(themeInfo, booleanValue3);
                                    this.rightActionBarLayout.animateThemedValues(themeInfo, booleanValue3);
                                    return;
                                    n3 = (Integer)array[0];
                                    childCount = sideMenu3.getChildCount();
                                    connectionState = n;
                                    break Label_1414;
                                    Label_1380:
                                    break Block_50;
                                    child2.invalidate();
                                    return;
                                }
                                themeInfo = (Theme.ThemeInfo)array[0];
                                booleanValue3 = (boolean)array[1];
                                this.actionBarLayout.animateThemedValues(themeInfo, booleanValue3);
                                continue Block_51_Outer;
                            }
                            sideMenu3 = this.sideMenu;
                            continue Label_1414_Outer;
                        }
                        child2 = this.sideMenu.getChildAt(connectionState);
                        continue;
                    }
                    Label_1460:
                    ++connectionState;
                    continue Label_1414;
                }
            }
            // iftrue(Label_1466:, connectionState >= childCount)
            // iftrue(Label_1460:, !child2 instanceof DrawerUserCell || (DrawerUserCell)child2.getAccountNumber() != n3.intValue())
            catch (Exception ex3) {
                continue;
            }
            break;
        }
    }
    
    public void didSelectDialogs(final DialogsActivity dialogsActivity, final ArrayList<Long> list, final CharSequence charSequence, final boolean b) {
        final int n = 0;
        final long longValue = list.get(0);
        final int n2 = (int)longValue;
        final int n3 = (int)(longValue >> 32);
        final Bundle bundle = new Bundle();
        int n4;
        if (dialogsActivity != null) {
            n4 = dialogsActivity.getCurrentAccount();
        }
        else {
            n4 = this.currentAccount;
        }
        bundle.putBoolean("scrollToTopOnResume", true);
        if (!AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(n4).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        }
        if (n2 != 0) {
            if (n3 == 1) {
                bundle.putInt("chat_id", n2);
            }
            else if (n2 > 0) {
                bundle.putInt("user_id", n2);
            }
            else if (n2 < 0) {
                bundle.putInt("chat_id", -n2);
            }
        }
        else {
            bundle.putInt("enc_id", n3);
        }
        if (!MessagesController.getInstance(n4).checkCanOpenChat(bundle, dialogsActivity)) {
            return;
        }
        final ChatActivity chatActivity = new ChatActivity(bundle);
        final ArrayList<TLRPC.User> contactsToSend = this.contactsToSend;
        if (contactsToSend != null && contactsToSend.size() == 1) {
            if (this.contactsToSend.size() == 1) {
                final PhonebookShareActivity phonebookShareActivity = new PhonebookShareActivity(null, this.contactsToSendUri, null, null);
                phonebookShareActivity.setDelegate(new _$$Lambda$LaunchActivity$Uzy9GRHh4_9QiCM66VVA1Ftqfj0(this, chatActivity, n4, longValue));
                this.actionBarLayout.presentFragment(phonebookShareActivity, dialogsActivity != null, dialogsActivity == null, true, false);
            }
        }
        else {
            final ActionBarLayout actionBarLayout = this.actionBarLayout;
            final boolean b2 = dialogsActivity != null;
            final boolean b3 = dialogsActivity == null;
            final String s = null;
            actionBarLayout.presentFragment(chatActivity, b2, b3, true, false);
            final String videoPath = this.videoPath;
            if (videoPath != null) {
                chatActivity.openVideoEditor(videoPath, this.sendingText);
                this.sendingText = s;
            }
            if (this.photoPathsArray != null) {
                final String sendingText = this.sendingText;
                if (sendingText != null && sendingText.length() <= 1024 && this.photoPathsArray.size() == 1) {
                    this.photoPathsArray.get(0).caption = this.sendingText;
                    this.sendingText = s;
                }
                SendMessagesHelper.prepareSendingMedia(this.photoPathsArray, longValue, null, null, false, false, null);
            }
            if (this.documentsPathsArray != null || this.documentsUrisArray != null) {
                final String sendingText2 = this.sendingText;
                String sendingText3 = null;
                Label_0527: {
                    if (sendingText2 != null && sendingText2.length() <= 1024) {
                        final ArrayList<String> documentsPathsArray = this.documentsPathsArray;
                        int size;
                        if (documentsPathsArray != null) {
                            size = documentsPathsArray.size();
                        }
                        else {
                            size = 0;
                        }
                        final ArrayList<Uri> documentsUrisArray = this.documentsUrisArray;
                        int size2;
                        if (documentsUrisArray != null) {
                            size2 = documentsUrisArray.size();
                        }
                        else {
                            size2 = 0;
                        }
                        if (size + size2 == 1) {
                            sendingText3 = this.sendingText;
                            this.sendingText = s;
                            break Label_0527;
                        }
                    }
                    sendingText3 = s;
                }
                SendMessagesHelper.prepareSendingDocuments(this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, sendingText3, this.documentsMimeType, longValue, null, null, null);
            }
            final Location sendingLocation = this.sendingLocation;
            if (sendingLocation != null) {
                SendMessagesHelper.prepareSendingLocation(sendingLocation, longValue);
                this.sendingText = s;
            }
            final String sendingText4 = this.sendingText;
            if (sendingText4 != null) {
                SendMessagesHelper.prepareSendingText(sendingText4, longValue);
            }
            final ArrayList<TLRPC.User> contactsToSend2 = this.contactsToSend;
            if (contactsToSend2 != null && !contactsToSend2.isEmpty()) {
                for (int i = n; i < this.contactsToSend.size(); ++i) {
                    SendMessagesHelper.getInstance(n4).sendMessage(this.contactsToSend.get(i), longValue, null, null, null);
                }
            }
        }
        this.photoPathsArray = null;
        this.videoPath = null;
        this.sendingText = null;
        this.sendingLocation = null;
        this.documentsPathsArray = null;
        this.documentsOriginalPathsArray = null;
        this.contactsToSend = null;
        this.contactsToSendUri = null;
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        keyEvent.getKeyCode();
        if (!LaunchActivity.mainFragmentsStack.isEmpty() && (!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isVisible()) && keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 0 && (keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25)) {
            final ArrayList<BaseFragment> mainFragmentsStack = LaunchActivity.mainFragmentsStack;
            final BaseFragment baseFragment = mainFragmentsStack.get(mainFragmentsStack.size() - 1);
            if (baseFragment instanceof ChatActivity && ((ChatActivity)baseFragment).maybePlayVisibleVideo()) {
                return true;
            }
            if (AndroidUtilities.isTablet() && !LaunchActivity.rightFragmentsStack.isEmpty()) {
                final ArrayList<BaseFragment> rightFragmentsStack = LaunchActivity.rightFragmentsStack;
                final BaseFragment baseFragment2 = rightFragmentsStack.get(rightFragmentsStack.size() - 1);
                if (baseFragment2 instanceof ChatActivity && ((ChatActivity)baseFragment2).maybePlayVisibleVideo()) {
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }
    
    public ActionBarLayout getActionBarLayout() {
        return this.actionBarLayout;
    }
    
    public ActionBarLayout getLayersActionBarLayout() {
        return this.layersActionBarLayout;
    }
    
    public int getMainFragmentsCount() {
        return LaunchActivity.mainFragmentsStack.size();
    }
    
    public ActionBarLayout getRightActionBarLayout() {
        return this.rightActionBarLayout;
    }
    
    public void hideVisibleActionMode() {
        final ActionMode visibleActionMode = this.visibleActionMode;
        if (visibleActionMode == null) {
            return;
        }
        visibleActionMode.finish();
    }
    
    public boolean needAddFragmentToStack(final BaseFragment baseFragment, final ActionBarLayout actionBarLayout) {
        if (AndroidUtilities.isTablet()) {
            final DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
            final boolean b = baseFragment instanceof LoginActivity;
            drawerLayoutContainer.setAllowOpenDrawer(!b && !(baseFragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0, true);
            if (baseFragment instanceof DialogsActivity) {
                if (((DialogsActivity)baseFragment).isMainDialogList()) {
                    final ActionBarLayout actionBarLayout2 = this.actionBarLayout;
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
            }
            else if (baseFragment instanceof ChatActivity) {
                if (!this.tabletFullSize) {
                    final ActionBarLayout rightActionBarLayout = this.rightActionBarLayout;
                    if (actionBarLayout != rightActionBarLayout) {
                        rightActionBarLayout.setVisibility(0);
                        this.backgroundTablet.setVisibility(8);
                        this.rightActionBarLayout.removeAllFragments();
                        this.rightActionBarLayout.addFragmentToStack(baseFragment);
                        if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                            while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                                final ActionBarLayout layersActionBarLayout = this.layersActionBarLayout;
                                layersActionBarLayout.removeFragmentFromStack(layersActionBarLayout.fragmentsStack.get(0));
                            }
                            this.layersActionBarLayout.closeLastFragment(true);
                        }
                        return false;
                    }
                }
                if (this.tabletFullSize) {
                    final ActionBarLayout actionBarLayout3 = this.actionBarLayout;
                    if (actionBarLayout != actionBarLayout3) {
                        actionBarLayout3.addFragmentToStack(baseFragment);
                        if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                            while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                                final ActionBarLayout layersActionBarLayout2 = this.layersActionBarLayout;
                                layersActionBarLayout2.removeFragmentFromStack(layersActionBarLayout2.fragmentsStack.get(0));
                            }
                            this.layersActionBarLayout.closeLastFragment(true);
                        }
                        return false;
                    }
                }
            }
            else {
                final ActionBarLayout layersActionBarLayout3 = this.layersActionBarLayout;
                if (actionBarLayout != layersActionBarLayout3) {
                    layersActionBarLayout3.setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    if (b) {
                        this.backgroundTablet.setVisibility(0);
                        this.shadowTabletSide.setVisibility(8);
                        this.shadowTablet.setBackgroundColor(0);
                    }
                    else {
                        this.shadowTablet.setBackgroundColor(2130706432);
                    }
                    this.layersActionBarLayout.addFragmentToStack(baseFragment);
                    return false;
                }
            }
            return true;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer((baseFragment instanceof LoginActivity) ? (LaunchActivity.mainFragmentsStack.size() != 0) : (!(baseFragment instanceof CountrySelectActivity) || LaunchActivity.mainFragmentsStack.size() != 1), false);
        return true;
    }
    
    public boolean needCloseLastFragment(final ActionBarLayout actionBarLayout) {
        if (AndroidUtilities.isTablet()) {
            if (actionBarLayout == this.actionBarLayout && actionBarLayout.fragmentsStack.size() <= 1) {
                this.onFinish();
                this.finish();
                return false;
            }
            if (actionBarLayout == this.rightActionBarLayout) {
                if (!this.tabletFullSize) {
                    this.backgroundTablet.setVisibility(0);
                }
            }
            else if (actionBarLayout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                this.onFinish();
                this.finish();
                return false;
            }
        }
        else {
            if (actionBarLayout.fragmentsStack.size() <= 1) {
                this.onFinish();
                this.finish();
                return false;
            }
            if (actionBarLayout.fragmentsStack.size() >= 2 && !(actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity)) {
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            }
        }
        return true;
    }
    
    public boolean needPresentFragment(final BaseFragment baseFragment, final boolean b, final boolean b2, ActionBarLayout actionBarLayout) {
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (!AndroidUtilities.isTablet()) {
            this.drawerLayoutContainer.setAllowOpenDrawer((baseFragment instanceof LoginActivity) ? (LaunchActivity.mainFragmentsStack.size() != 0) : (!(baseFragment instanceof CountrySelectActivity) || LaunchActivity.mainFragmentsStack.size() != 1), false);
            return true;
        }
        final DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
        final boolean b3 = baseFragment instanceof LoginActivity;
        drawerLayoutContainer.setAllowOpenDrawer(!b3 && !(baseFragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0, true);
        if (baseFragment instanceof DialogsActivity && ((DialogsActivity)baseFragment).isMainDialogList()) {
            final ActionBarLayout actionBarLayout2 = this.actionBarLayout;
            if (actionBarLayout != actionBarLayout2) {
                actionBarLayout2.removeAllFragments();
                this.actionBarLayout.presentFragment(baseFragment, b, b2, false, false);
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
        if (baseFragment instanceof ChatActivity) {
            if ((!this.tabletFullSize && actionBarLayout == this.rightActionBarLayout) || (this.tabletFullSize && actionBarLayout == this.actionBarLayout)) {
                boolean b4 = false;
                Label_0272: {
                    if (this.tabletFullSize) {
                        final ActionBarLayout actionBarLayout3 = this.actionBarLayout;
                        if (actionBarLayout == actionBarLayout3) {
                            if (actionBarLayout3.fragmentsStack.size() == 1) {
                                b4 = false;
                                break Label_0272;
                            }
                        }
                    }
                    b4 = true;
                }
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        actionBarLayout = this.layersActionBarLayout;
                        actionBarLayout.removeFragmentFromStack(actionBarLayout.fragmentsStack.get(0));
                    }
                    this.layersActionBarLayout.closeLastFragment(b2 ^ true);
                }
                if (!b4) {
                    this.actionBarLayout.presentFragment(baseFragment, false, b2, false, false);
                }
                return b4;
            }
            if (!this.tabletFullSize) {
                final ActionBarLayout rightActionBarLayout = this.rightActionBarLayout;
                if (actionBarLayout != rightActionBarLayout) {
                    rightActionBarLayout.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(baseFragment, b, true, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            final ActionBarLayout layersActionBarLayout = this.layersActionBarLayout;
                            layersActionBarLayout.removeFragmentFromStack(layersActionBarLayout.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(b2 ^ true);
                    }
                    return false;
                }
            }
            if (this.tabletFullSize) {
                final ActionBarLayout actionBarLayout4 = this.actionBarLayout;
                if (actionBarLayout != actionBarLayout4) {
                    actionBarLayout4.presentFragment(baseFragment, actionBarLayout4.fragmentsStack.size() > 1, b2, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            final ActionBarLayout layersActionBarLayout2 = this.layersActionBarLayout;
                            layersActionBarLayout2.removeFragmentFromStack(layersActionBarLayout2.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(b2 ^ true);
                    }
                    return false;
                }
            }
            if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                    actionBarLayout = this.layersActionBarLayout;
                    actionBarLayout.removeFragmentFromStack(actionBarLayout.fragmentsStack.get(0));
                }
                this.layersActionBarLayout.closeLastFragment(b2 ^ true);
            }
            actionBarLayout = this.actionBarLayout;
            actionBarLayout.presentFragment(baseFragment, actionBarLayout.fragmentsStack.size() > 1, b2, false, false);
            return false;
        }
        else {
            final ActionBarLayout layersActionBarLayout3 = this.layersActionBarLayout;
            if (actionBarLayout != layersActionBarLayout3) {
                layersActionBarLayout3.setVisibility(0);
                this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                if (b3) {
                    this.backgroundTablet.setVisibility(0);
                    this.shadowTabletSide.setVisibility(8);
                    this.shadowTablet.setBackgroundColor(0);
                }
                else {
                    this.shadowTablet.setBackgroundColor(2130706432);
                }
                this.layersActionBarLayout.presentFragment(baseFragment, b, b2, false, false);
                return false;
            }
            return true;
        }
    }
    
    public void onActionModeFinished(final ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        if (this.visibleActionMode == actionMode) {
            this.visibleActionMode = null;
        }
        if (Build$VERSION.SDK_INT >= 23 && actionMode.getType() == 1) {
            return;
        }
        this.actionBarLayout.onActionModeFinished(actionMode);
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onActionModeFinished(actionMode);
            this.layersActionBarLayout.onActionModeFinished(actionMode);
        }
    }
    
    public void onActionModeStarted(final ActionMode visibleActionMode) {
        super.onActionModeStarted(visibleActionMode);
        this.visibleActionMode = visibleActionMode;
        try {
            final Menu menu = visibleActionMode.getMenu();
            if (menu != null && !this.actionBarLayout.extendActionMode(menu) && AndroidUtilities.isTablet() && !this.rightActionBarLayout.extendActionMode(menu)) {
                this.layersActionBarLayout.extendActionMode(menu);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        if (Build$VERSION.SDK_INT >= 23 && visibleActionMode.getType() == 1) {
            return;
        }
        this.actionBarLayout.onActionModeStarted(visibleActionMode);
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onActionModeStarted(visibleActionMode);
            this.layersActionBarLayout.onActionModeStarted(visibleActionMode);
        }
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        super.onActivityResult(n, n2, intent);
        final ThemeEditorView instance = ThemeEditorView.getInstance();
        if (instance != null) {
            instance.onActivityResult(n, n2, intent);
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            final ArrayList<BaseFragment> fragmentsStack = this.actionBarLayout.fragmentsStack;
            fragmentsStack.get(fragmentsStack.size() - 1).onActivityResultFragment(n, n2, intent);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                final ArrayList<BaseFragment> fragmentsStack2 = this.rightActionBarLayout.fragmentsStack;
                fragmentsStack2.get(fragmentsStack2.size() - 1).onActivityResultFragment(n, n2, intent);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                final ArrayList<BaseFragment> fragmentsStack3 = this.layersActionBarLayout.fragmentsStack;
                fragmentsStack3.get(fragmentsStack3.size() - 1).onActivityResultFragment(n, n2, intent);
            }
        }
    }
    
    public void onBackPressed() {
        if (this.passcodeView.getVisibility() == 0) {
            this.finish();
            return;
        }
        final boolean hasInstance = SecretMediaViewer.hasInstance();
        final boolean b = false;
        if (hasInstance && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
        }
        else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        }
        else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(true, false);
        }
        else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
        }
        else if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
                this.layersActionBarLayout.onBackPressed();
            }
            else {
                int n = b ? 1 : 0;
                if (this.rightActionBarLayout.getVisibility() == 0) {
                    n = (b ? 1 : 0);
                    if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        final ArrayList<BaseFragment> fragmentsStack = this.rightActionBarLayout.fragmentsStack;
                        n = ((fragmentsStack.get(fragmentsStack.size() - 1).onBackPressed() ^ true) ? 1 : 0);
                    }
                }
                if (n == 0) {
                    this.actionBarLayout.onBackPressed();
                }
            }
        }
        else {
            this.actionBarLayout.onBackPressed();
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        AndroidUtilities.checkDisplaySize((Context)this, configuration);
        super.onConfigurationChanged(configuration);
        this.checkLayout();
        final PipRoundVideoView instance = PipRoundVideoView.getInstance();
        if (instance != null) {
            instance.onConfigurationChanged();
        }
        final EmbedBottomSheet instance2 = EmbedBottomSheet.getInstance();
        if (instance2 != null) {
            instance2.onConfigurationChanged(configuration);
        }
        final PhotoViewer pipInstance = PhotoViewer.getPipInstance();
        if (pipInstance != null) {
            pipInstance.onConfigurationChanged(configuration);
        }
        final ThemeEditorView instance3 = ThemeEditorView.getInstance();
        if (instance3 != null) {
            instance3.onConfigurationChanged();
        }
    }
    
    protected void onCreate(Bundle lowerCase) {
        ApplicationLoader.postInitApplication();
        AndroidUtilities.checkDisplaySize((Context)this, this.getResources().getConfiguration());
        this.currentAccount = UserConfig.selectedAccount;
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            final Intent intent = this.getIntent();
            boolean b = false;
            Label_0168: {
                if (intent != null && intent.getAction() != null) {
                    if ("android.intent.action.SEND".equals(intent.getAction()) || "android.intent.action.SEND_MULTIPLE".equals(intent.getAction())) {
                        super.onCreate((Bundle)lowerCase);
                        this.finish();
                        return;
                    }
                    if ("android.intent.action.VIEW".equals(intent.getAction())) {
                        final Uri data = intent.getData();
                        if (data != null) {
                            final String lowerCase2 = data.toString().toLowerCase();
                            if (lowerCase2.startsWith("tg:proxy") || lowerCase2.startsWith("tg://proxy") || lowerCase2.startsWith("tg:socks") || lowerCase2.startsWith("tg://socks")) {
                                b = true;
                                break Label_0168;
                            }
                        }
                    }
                }
                b = false;
            }
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            final long long1 = globalMainSettings.getLong("intro_crashed_time", 0L);
            final boolean booleanExtra = intent.getBooleanExtra("fromIntro", false);
            if (booleanExtra) {
                globalMainSettings.edit().putLong("intro_crashed_time", 0L).commit();
            }
            if (!b && Math.abs(long1 - System.currentTimeMillis()) >= 120000L && intent != null && !booleanExtra && ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).getAll().isEmpty()) {
                final Intent intent2 = new Intent((Context)this, (Class)IntroActivity.class);
                intent2.setData(intent.getData());
                this.startActivity(intent2);
                super.onCreate((Bundle)lowerCase);
                this.finish();
                return;
            }
        }
        this.requestWindowFeature(1);
        this.setTheme(2131624206);
        Label_0369: {
            if (Build$VERSION.SDK_INT < 21) {
                break Label_0369;
            }
            while (true) {
                try {
                    this.setTaskDescription(new ActivityManager$TaskDescription((String)null, (Bitmap)null, Theme.getColor("actionBarDefault") | 0xFF000000));
                    try {
                        this.getWindow().setNavigationBarColor(-16777216);
                    }
                    catch (Exception ex3) {}
                    this.getWindow().setBackgroundDrawableResource(2131165891);
                    if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
                        try {
                            this.getWindow().setFlags(8192, 8192);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                    super.onCreate((Bundle)lowerCase);
                    if (Build$VERSION.SDK_INT >= 24) {
                        AndroidUtilities.isInMultiwindow = this.isInMultiWindowMode();
                    }
                    Theme.createChatResources((Context)this, false);
                    if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.appLocked) {
                        SharedConfig.lastPauseTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                    }
                    final int identifier = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                    if (identifier > 0) {
                        AndroidUtilities.statusBarHeight = this.getResources().getDimensionPixelSize(identifier);
                    }
                    this.actionBarLayout = new ActionBarLayout((Context)this);
                    (this.drawerLayoutContainer = new DrawerLayoutContainer((Context)this)).setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
                    this.setContentView((View)this.drawerLayoutContainer, new ViewGroup$LayoutParams(-1, -1));
                    if (AndroidUtilities.isTablet()) {
                        this.getWindow().setSoftInputMode(16);
                        final RelativeLayout relativeLayout = new RelativeLayout(this) {
                            private boolean inLayout;
                            
                            protected void onLayout(final boolean b, int dp, int n, int n2, final int n3) {
                                final int n4 = n2 - dp;
                                if (!AndroidUtilities.isInMultiwindow && (!AndroidUtilities.isSmallTablet() || this.getResources().getConfiguration().orientation == 2)) {
                                    n2 = n4 / 100 * 35;
                                    if ((dp = n2) < AndroidUtilities.dp(320.0f)) {
                                        dp = AndroidUtilities.dp(320.0f);
                                    }
                                    LaunchActivity.this.shadowTabletSide.layout(dp, 0, LaunchActivity.this.shadowTabletSide.getMeasuredWidth() + dp, LaunchActivity.this.shadowTabletSide.getMeasuredHeight());
                                    LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                                    LaunchActivity.this.rightActionBarLayout.layout(dp, 0, LaunchActivity.this.rightActionBarLayout.getMeasuredWidth() + dp, LaunchActivity.this.rightActionBarLayout.getMeasuredHeight());
                                }
                                else {
                                    LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                                }
                                dp = (n4 - LaunchActivity.this.layersActionBarLayout.getMeasuredWidth()) / 2;
                                n = (n3 - n - LaunchActivity.this.layersActionBarLayout.getMeasuredHeight()) / 2;
                                LaunchActivity.this.layersActionBarLayout.layout(dp, n, LaunchActivity.this.layersActionBarLayout.getMeasuredWidth() + dp, LaunchActivity.this.layersActionBarLayout.getMeasuredHeight() + n);
                                LaunchActivity.this.backgroundTablet.layout(0, 0, LaunchActivity.this.backgroundTablet.getMeasuredWidth(), LaunchActivity.this.backgroundTablet.getMeasuredHeight());
                                LaunchActivity.this.shadowTablet.layout(0, 0, LaunchActivity.this.shadowTablet.getMeasuredWidth(), LaunchActivity.this.shadowTablet.getMeasuredHeight());
                            }
                            
                            protected void onMeasure(int dp, int n) {
                                this.inLayout = true;
                                final int size = View$MeasureSpec.getSize(dp);
                                final int size2 = View$MeasureSpec.getSize(n);
                                this.setMeasuredDimension(size, size2);
                                if (!AndroidUtilities.isInMultiwindow && (!AndroidUtilities.isSmallTablet() || this.getResources().getConfiguration().orientation == 2)) {
                                    LaunchActivity.this.tabletFullSize = false;
                                    n = size / 100 * 35;
                                    if ((dp = n) < AndroidUtilities.dp(320.0f)) {
                                        dp = AndroidUtilities.dp(320.0f);
                                    }
                                    LaunchActivity.this.actionBarLayout.measure(View$MeasureSpec.makeMeasureSpec(dp, 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 1073741824));
                                    LaunchActivity.this.shadowTabletSide.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 1073741824));
                                    LaunchActivity.this.rightActionBarLayout.measure(View$MeasureSpec.makeMeasureSpec(size - dp, 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 1073741824));
                                }
                                else {
                                    LaunchActivity.this.tabletFullSize = true;
                                    LaunchActivity.this.actionBarLayout.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 1073741824));
                                }
                                LaunchActivity.this.backgroundTablet.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 1073741824));
                                LaunchActivity.this.shadowTablet.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 1073741824));
                                LaunchActivity.this.layersActionBarLayout.measure(View$MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(530.0f), size), 1073741824), View$MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(528.0f), size2), 1073741824));
                                this.inLayout = false;
                            }
                            
                            public void requestLayout() {
                                if (this.inLayout) {
                                    return;
                                }
                                super.requestLayout();
                            }
                        };
                        this.drawerLayoutContainer.addView((View)relativeLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                        this.backgroundTablet = new View((Context)this);
                        final BitmapDrawable backgroundDrawable = (BitmapDrawable)this.getResources().getDrawable(2131165338);
                        final Shader$TileMode repeat = Shader$TileMode.REPEAT;
                        backgroundDrawable.setTileModeXY(repeat, repeat);
                        this.backgroundTablet.setBackgroundDrawable((Drawable)backgroundDrawable);
                        relativeLayout.addView(this.backgroundTablet, (ViewGroup$LayoutParams)LayoutHelper.createRelative(-1, -1));
                        relativeLayout.addView((View)this.actionBarLayout);
                        (this.rightActionBarLayout = new ActionBarLayout((Context)this)).init(LaunchActivity.rightFragmentsStack);
                        this.rightActionBarLayout.setDelegate((ActionBarLayout.ActionBarLayoutDelegate)this);
                        relativeLayout.addView((View)this.rightActionBarLayout);
                        (this.shadowTabletSide = new FrameLayout((Context)this)).setBackgroundColor(1076449908);
                        relativeLayout.addView((View)this.shadowTabletSide);
                        this.shadowTablet = new FrameLayout((Context)this);
                        final FrameLayout shadowTablet = this.shadowTablet;
                        final boolean empty = LaunchActivity.layerFragmentsStack.isEmpty();
                        final int n = 8;
                        int visibility;
                        if (empty) {
                            visibility = 8;
                        }
                        else {
                            visibility = 0;
                        }
                        shadowTablet.setVisibility(visibility);
                        this.shadowTablet.setBackgroundColor(2130706432);
                        relativeLayout.addView((View)this.shadowTablet);
                        this.shadowTablet.setOnTouchListener((View$OnTouchListener)new _$$Lambda$LaunchActivity$KFZR9bOIUYM1vrC9qoPrRupqDO4(this));
                        this.shadowTablet.setOnClickListener((View$OnClickListener)_$$Lambda$LaunchActivity$OJponKw8R53ezoQT8H7udVOmkKQ.INSTANCE);
                        (this.layersActionBarLayout = new ActionBarLayout((Context)this)).setRemoveActionBarExtraHeight(true);
                        this.layersActionBarLayout.setBackgroundView((View)this.shadowTablet);
                        this.layersActionBarLayout.setUseAlphaAnimations(true);
                        this.layersActionBarLayout.setBackgroundResource(2131165322);
                        this.layersActionBarLayout.init(LaunchActivity.layerFragmentsStack);
                        this.layersActionBarLayout.setDelegate((ActionBarLayout.ActionBarLayoutDelegate)this);
                        this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
                        final ActionBarLayout layersActionBarLayout = this.layersActionBarLayout;
                        int visibility2;
                        if (LaunchActivity.layerFragmentsStack.isEmpty()) {
                            visibility2 = n;
                        }
                        else {
                            visibility2 = 0;
                        }
                        layersActionBarLayout.setVisibility(visibility2);
                        relativeLayout.addView((View)this.layersActionBarLayout);
                    }
                    else {
                        this.drawerLayoutContainer.addView((View)this.actionBarLayout, new ViewGroup$LayoutParams(-1, -1));
                    }
                    this.sideMenu = new RecyclerListView((Context)this);
                    ((DefaultItemAnimator)this.sideMenu.getItemAnimator()).setDelayAnimations(false);
                    this.sideMenu.setBackgroundColor(Theme.getColor("chats_menuBackground"));
                    this.sideMenu.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this, 1, false));
                    this.sideMenu.setAdapter(this.drawerLayoutAdapter = new DrawerLayoutAdapter((Context)this));
                    this.drawerLayoutContainer.setDrawerLayout(this.sideMenu);
                    final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.sideMenu.getLayoutParams();
                    final Point realScreenSize = AndroidUtilities.getRealScreenSize();
                    int width;
                    if (AndroidUtilities.isTablet()) {
                        width = AndroidUtilities.dp(320.0f);
                    }
                    else {
                        width = Math.min(AndroidUtilities.dp(320.0f), Math.min(realScreenSize.x, realScreenSize.y) - AndroidUtilities.dp(56.0f));
                    }
                    layoutParams.width = width;
                    layoutParams.height = -1;
                    this.sideMenu.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    this.sideMenu.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$LaunchActivity$dOtJNBBcNQv2FwIcA_NKr5dzUI0(this));
                    this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
                    this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
                    this.actionBarLayout.init(LaunchActivity.mainFragmentsStack);
                    this.actionBarLayout.setDelegate((ActionBarLayout.ActionBarLayoutDelegate)this);
                    Theme.loadWallpaper();
                    this.passcodeView = new PasscodeView((Context)this);
                    this.drawerLayoutContainer.addView((View)this.passcodeView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                    this.checkCurrentAccount();
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, this);
                    this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needShowAlert);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.reloadInterface);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeOtherAppActivities);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.notificationsCountUpdated);
                    if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                            this.actionBarLayout.addFragmentToStack(new LoginActivity());
                            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                        }
                        else {
                            final DialogsActivity dialogsActivity = new DialogsActivity(null);
                            dialogsActivity.setSideMenu(this.sideMenu);
                            this.actionBarLayout.addFragmentToStack(dialogsActivity);
                            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                        }
                        if (lowerCase != null) {
                            try {
                                final String string = ((Bundle)lowerCase).getString("fragment");
                                if (string != null) {
                                    final Bundle bundle = ((Bundle)lowerCase).getBundle("args");
                                    int n2 = 0;
                                    Label_1626: {
                                        switch (string.hashCode()) {
                                            case 1434631203: {
                                                if (string.equals("settings")) {
                                                    n2 = 1;
                                                    break Label_1626;
                                                }
                                                break;
                                            }
                                            case 738950403: {
                                                if (string.equals("channel")) {
                                                    n2 = 3;
                                                    break Label_1626;
                                                }
                                                break;
                                            }
                                            case 98629247: {
                                                if (string.equals("group")) {
                                                    n2 = 2;
                                                    break Label_1626;
                                                }
                                                break;
                                            }
                                            case 3052376: {
                                                if (string.equals("chat")) {
                                                    n2 = 0;
                                                    break Label_1626;
                                                }
                                                break;
                                            }
                                            case -1349522494: {
                                                if (string.equals("chat_profile")) {
                                                    n2 = 4;
                                                    break Label_1626;
                                                }
                                                break;
                                            }
                                            case -1529105743: {
                                                if (string.equals("wallpapers")) {
                                                    n2 = 5;
                                                    break Label_1626;
                                                }
                                                break;
                                            }
                                        }
                                        n2 = -1;
                                    }
                                    if (n2 != 0) {
                                        if (n2 != 1) {
                                            if (n2 != 2) {
                                                if (n2 != 3) {
                                                    if (n2 != 4) {
                                                        if (n2 == 5) {
                                                            final WallpapersListActivity wallpapersListActivity = new WallpapersListActivity(0);
                                                            this.actionBarLayout.addFragmentToStack(wallpapersListActivity);
                                                            wallpapersListActivity.restoreSelfArgs((Bundle)lowerCase);
                                                        }
                                                    }
                                                    else if (bundle != null) {
                                                        final ProfileActivity profileActivity = new ProfileActivity(bundle);
                                                        if (this.actionBarLayout.addFragmentToStack(profileActivity)) {
                                                            profileActivity.restoreSelfArgs((Bundle)lowerCase);
                                                        }
                                                    }
                                                }
                                                else if (bundle != null) {
                                                    final ChannelCreateActivity channelCreateActivity = new ChannelCreateActivity(bundle);
                                                    if (this.actionBarLayout.addFragmentToStack(channelCreateActivity)) {
                                                        channelCreateActivity.restoreSelfArgs((Bundle)lowerCase);
                                                    }
                                                }
                                            }
                                            else if (bundle != null) {
                                                final GroupCreateFinalActivity groupCreateFinalActivity = new GroupCreateFinalActivity(bundle);
                                                if (this.actionBarLayout.addFragmentToStack(groupCreateFinalActivity)) {
                                                    groupCreateFinalActivity.restoreSelfArgs((Bundle)lowerCase);
                                                }
                                            }
                                        }
                                        else {
                                            final SettingsActivity settingsActivity = new SettingsActivity();
                                            this.actionBarLayout.addFragmentToStack(settingsActivity);
                                            settingsActivity.restoreSelfArgs((Bundle)lowerCase);
                                        }
                                    }
                                    else if (bundle != null) {
                                        final ChatActivity chatActivity = new ChatActivity(bundle);
                                        if (this.actionBarLayout.addFragmentToStack(chatActivity)) {
                                            chatActivity.restoreSelfArgs((Bundle)lowerCase);
                                        }
                                    }
                                }
                            }
                            catch (Exception ex2) {
                                FileLog.e(ex2);
                            }
                        }
                    }
                    else {
                        final BaseFragment baseFragment = this.actionBarLayout.fragmentsStack.get(0);
                        if (baseFragment instanceof DialogsActivity) {
                            ((DialogsActivity)baseFragment).setSideMenu(this.sideMenu);
                        }
                        boolean b3;
                        if (AndroidUtilities.isTablet()) {
                            final boolean b2 = b3 = (this.actionBarLayout.fragmentsStack.size() <= 1 && this.layersActionBarLayout.fragmentsStack.isEmpty());
                            if (this.layersActionBarLayout.fragmentsStack.size() == 1) {
                                b3 = b2;
                                if (this.layersActionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) {
                                    b3 = false;
                                }
                            }
                        }
                        else {
                            b3 = true;
                        }
                        boolean b4 = b3;
                        if (this.actionBarLayout.fragmentsStack.size() == 1) {
                            b4 = b3;
                            if (this.actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) {
                                b4 = false;
                            }
                        }
                        this.drawerLayoutContainer.setAllowOpenDrawer(b4, false);
                    }
                    this.checkLayout();
                    this.handleIntent(this.getIntent(), false, lowerCase != null, false);
                    try {
                        final String display = Build.DISPLAY;
                        final String user = Build.USER;
                        String lowerCase3 = "";
                        if (display != null) {
                            lowerCase = (Exception)display.toLowerCase();
                        }
                        else {
                            lowerCase = (Exception)"";
                        }
                        if (user != null) {
                            lowerCase3 = ((String)lowerCase).toLowerCase();
                        }
                        if (((String)lowerCase).contains("flyme") || lowerCase3.contains("flyme")) {
                            AndroidUtilities.incorrectDisplaySizeFix = true;
                            final View rootView = this.getWindow().getDecorView().getRootView();
                            final ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                            lowerCase = (Exception)new _$$Lambda$LaunchActivity$KOGX7F1UQC0GXDka6tTbIrU0Wk0(rootView);
                            viewTreeObserver.addOnGlobalLayoutListener(this.onGlobalLayoutListener = (ViewTreeObserver$OnGlobalLayoutListener)lowerCase);
                        }
                    }
                    catch (Exception lowerCase) {
                        FileLog.e(lowerCase);
                    }
                    MediaController.getInstance().setBaseActivity(this, true);
                }
                catch (Exception ex4) {
                    continue;
                }
                break;
            }
        }
    }
    
    protected void onDestroy() {
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
        final PipRoundVideoView instance = PipRoundVideoView.getInstance();
        MediaController.getInstance().setBaseActivity(this, false);
        MediaController.getInstance().setFeedbackView((View)this.actionBarLayout, false);
        if (instance != null) {
            instance.close(false);
        }
        Theme.destroyResources();
        final EmbedBottomSheet instance2 = EmbedBottomSheet.getInstance();
        if (instance2 != null) {
            instance2.destroy();
        }
        final ThemeEditorView instance3 = ThemeEditorView.getInstance();
        if (instance3 != null) {
            instance3.destroy();
        }
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            if (this.onGlobalLayoutListener != null) {
                this.getWindow().getDecorView().getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        super.onDestroy();
        this.onFinish();
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        if (n == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                return super.onKeyUp(n, keyEvent);
            }
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                return super.onKeyUp(n, keyEvent);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getVisibility() == 0 && !this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    this.layersActionBarLayout.onKeyUp(n, keyEvent);
                }
                else if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    this.rightActionBarLayout.onKeyUp(n, keyEvent);
                }
                else {
                    this.actionBarLayout.onKeyUp(n, keyEvent);
                }
            }
            else if (this.actionBarLayout.fragmentsStack.size() == 1) {
                if (!this.drawerLayoutContainer.isDrawerOpened()) {
                    if (this.getCurrentFocus() != null) {
                        AndroidUtilities.hideKeyboard(this.getCurrentFocus());
                    }
                    this.drawerLayoutContainer.openDrawer(false);
                }
                else {
                    this.drawerLayoutContainer.closeDrawer(false);
                }
            }
            else {
                this.actionBarLayout.onKeyUp(n, keyEvent);
            }
        }
        return super.onKeyUp(n, keyEvent);
    }
    
    public void onLowMemory() {
        super.onLowMemory();
        this.actionBarLayout.onLowMemory();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onLowMemory();
            this.layersActionBarLayout.onLowMemory();
        }
    }
    
    public void onMultiWindowModeChanged(final boolean isInMultiwindow) {
        AndroidUtilities.isInMultiwindow = isInMultiwindow;
        this.checkLayout();
    }
    
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.handleIntent(intent, true, false, false);
    }
    
    protected void onPause() {
        super.onPause();
        SharedConfig.lastAppPauseTime = System.currentTimeMillis();
        ApplicationLoader.mainInterfacePaused = true;
        Utilities.stageQueue.postRunnable((Runnable)_$$Lambda$LaunchActivity$uQqSZiudecpXZp8TwXxpaZLYG6E.INSTANCE);
        this.onPasscodePause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onPause();
            this.layersActionBarLayout.onPause();
        }
        final PasscodeView passcodeView = this.passcodeView;
        if (passcodeView != null) {
            passcodeView.onPause();
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        AndroidUtilities.unregisterUpdates();
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onPause();
        }
    }
    
    public boolean onPreIme() {
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
            return true;
        }
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
            return true;
        }
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(true, false);
            return true;
        }
        return false;
    }
    
    public void onRebuildAllFragments(final ActionBarLayout actionBarLayout, final boolean b) {
        if (AndroidUtilities.isTablet() && actionBarLayout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(b, b);
            this.actionBarLayout.rebuildAllFragmentViews(b, b);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
    }
    
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        super.onRequestPermissionsResult(n, array, array2);
        final boolean b = false;
        if (n != 3 && n != 4 && n != 5 && n != 19 && n != 20 && n != 22) {
            if (n == 2 && array2.length > 0 && array2[0] == 0) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.locationPermissionGranted, new Object[0]);
            }
        }
        else {
            int n2 = 0;
            Label_0172: {
                if (array2.length > 0 && array2[0] == 0) {
                    if (n == 4) {
                        ImageLoader.getInstance().checkMediaPaths();
                        return;
                    }
                    if (n == 5) {
                        ContactsController.getInstance(this.currentAccount).forceImportContacts();
                        return;
                    }
                    if (n == 3) {
                        if (SharedConfig.inappCamera) {
                            CameraController.getInstance().initCamera(null);
                        }
                        return;
                    }
                    n2 = (b ? 1 : 0);
                    if (n == 19) {
                        break Label_0172;
                    }
                    n2 = (b ? 1 : 0);
                    if (n == 20) {
                        break Label_0172;
                    }
                    if (n == 22) {
                        n2 = (b ? 1 : 0);
                        break Label_0172;
                    }
                }
                n2 = 1;
            }
            if (n2 != 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                if (n == 3) {
                    builder.setMessage(LocaleController.getString("PermissionNoAudio", 2131560414));
                }
                else if (n == 4) {
                    builder.setMessage(LocaleController.getString("PermissionStorage", 2131560420));
                }
                else if (n == 5) {
                    builder.setMessage(LocaleController.getString("PermissionContacts", 2131560412));
                }
                else if (n == 19 || n == 20 || n == 22) {
                    builder.setMessage(LocaleController.getString("PermissionNoCamera", 2131560416));
                }
                builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new _$$Lambda$LaunchActivity$4W20lwtGVi8T2FP_31dzdYvI9yo(this));
                builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                builder.show();
                return;
            }
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            final ArrayList<BaseFragment> fragmentsStack = this.actionBarLayout.fragmentsStack;
            fragmentsStack.get(fragmentsStack.size() - 1).onRequestPermissionsResultFragment(n, array, array2);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                final ArrayList<BaseFragment> fragmentsStack2 = this.rightActionBarLayout.fragmentsStack;
                fragmentsStack2.get(fragmentsStack2.size() - 1).onRequestPermissionsResultFragment(n, array, array2);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                final ArrayList<BaseFragment> fragmentsStack3 = this.layersActionBarLayout.fragmentsStack;
                fragmentsStack3.get(fragmentsStack3.size() - 1).onRequestPermissionsResultFragment(n, array, array2);
            }
        }
    }
    
    protected void onResume() {
        super.onResume();
        MediaController.getInstance().setFeedbackView((View)this.actionBarLayout, true);
        this.showLanguageAlert(ApplicationLoader.mainInterfacePaused = false);
        Utilities.stageQueue.postRunnable((Runnable)_$$Lambda$LaunchActivity$ZNfXQPqW9KpGIQ6C_so6U6aBbbU.INSTANCE);
        this.checkFreeDiscSpace();
        MediaController.checkGallery();
        this.onPasscodeResume();
        if (this.passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onResume();
                this.layersActionBarLayout.onResume();
            }
        }
        else {
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
        this.updateCurrentConnectionState(this.currentAccount);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onResume();
        }
        if (PipRoundVideoView.getInstance() != null && MediaController.getInstance().isMessagePaused()) {
            final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null) {
                MediaController.getInstance().seekToProgress(playingMessageObject, playingMessageObject.audioProgress);
            }
        }
        if (UserConfig.getInstance(UserConfig.selectedAccount).unacceptedTermsOfService != null) {
            final int selectedAccount = UserConfig.selectedAccount;
            this.showTosActivity(selectedAccount, UserConfig.getInstance(selectedAccount).unacceptedTermsOfService);
        }
        else if (UserConfig.getInstance(0).pendingAppUpdate != null) {
            this.showUpdateActivity(UserConfig.selectedAccount, UserConfig.getInstance(0).pendingAppUpdate);
        }
        this.checkAppUpdate(false);
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        try {
            super.onSaveInstanceState(bundle);
            BaseFragment baseFragment = null;
            if (AndroidUtilities.isTablet()) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    baseFragment = this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1);
                }
                else if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    baseFragment = this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1);
                }
                else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                    baseFragment = this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
                }
            }
            else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                baseFragment = this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
            }
            if (baseFragment != null) {
                final Bundle arguments = baseFragment.getArguments();
                if (baseFragment instanceof ChatActivity && arguments != null) {
                    bundle.putBundle("args", arguments);
                    bundle.putString("fragment", "chat");
                }
                else if (baseFragment instanceof SettingsActivity) {
                    bundle.putString("fragment", "settings");
                }
                else if (baseFragment instanceof GroupCreateFinalActivity && arguments != null) {
                    bundle.putBundle("args", arguments);
                    bundle.putString("fragment", "group");
                }
                else if (baseFragment instanceof WallpapersListActivity) {
                    bundle.putString("fragment", "wallpapers");
                }
                else if (baseFragment instanceof ProfileActivity && ((ProfileActivity)baseFragment).isChat() && arguments != null) {
                    bundle.putBundle("args", arguments);
                    bundle.putString("fragment", "chat_profile");
                }
                else if (baseFragment instanceof ChannelCreateActivity && arguments != null && arguments.getInt("step") == 0) {
                    bundle.putBundle("args", arguments);
                    bundle.putString("fragment", "channel");
                }
                baseFragment.saveSelfArgs(bundle);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    protected void onStart() {
        super.onStart();
        Browser.bindCustomTabsService(this);
    }
    
    protected void onStop() {
        super.onStop();
        Browser.unbindCustomTabsService(this);
    }
    
    public void presentFragment(final BaseFragment baseFragment) {
        this.actionBarLayout.presentFragment(baseFragment);
    }
    
    public boolean presentFragment(final BaseFragment baseFragment, final boolean b, final boolean b2) {
        return this.actionBarLayout.presentFragment(baseFragment, b, b2, true, false);
    }
    
    public void rebuildAllFragments(final boolean b) {
        final ActionBarLayout layersActionBarLayout = this.layersActionBarLayout;
        if (layersActionBarLayout != null) {
            layersActionBarLayout.rebuildAllFragmentViews(b, b);
        }
        else {
            this.actionBarLayout.rebuildAllFragmentViews(b, b);
        }
    }
    
    public AlertDialog showAlertDialog(final AlertDialog.Builder builder) {
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            (this.visibleDialog = builder.show()).setCanceledOnTouchOutside(true);
            this.visibleDialog.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
                public void onDismiss(final DialogInterface dialogInterface) {
                    if (LaunchActivity.this.visibleDialog != null) {
                        if (LaunchActivity.this.visibleDialog == LaunchActivity.this.localeDialog) {
                            try {
                                final String shortName = LocaleController.getInstance().getCurrentLocaleInfo().shortName;
                                final LaunchActivity this$0 = LaunchActivity.this;
                                final LaunchActivity this$2 = LaunchActivity.this;
                                HashMap hashMap;
                                if (shortName.equals("en")) {
                                    hashMap = LaunchActivity.this.englishLocaleStrings;
                                }
                                else {
                                    hashMap = LaunchActivity.this.systemLocaleStrings;
                                }
                                Toast.makeText((Context)this$0, (CharSequence)this$2.getStringForLanguageAlert(hashMap, "ChangeLanguageLater", 2131558906), 1).show();
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                            }
                            LaunchActivity.this.localeDialog = null;
                        }
                        else if (LaunchActivity.this.visibleDialog == LaunchActivity.this.proxyErrorDialog) {
                            MessagesController.getGlobalMainSettings();
                            final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
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
            });
            return this.visibleDialog;
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
            return null;
        }
    }
    
    public void switchToAccount(final int selectedAccount, final boolean b) {
        if (selectedAccount == UserConfig.selectedAccount) {
            return;
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        UserConfig.selectedAccount = selectedAccount;
        UserConfig.getInstance(0).saveConfig(false);
        this.checkCurrentAccount();
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
        if (b) {
            this.actionBarLayout.removeAllFragments();
        }
        else {
            this.actionBarLayout.removeFragmentFromStack(0);
        }
        final DialogsActivity dialogsActivity = new DialogsActivity(null);
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
        if (UserConfig.getInstance(selectedAccount).unacceptedTermsOfService != null) {
            this.showTosActivity(selectedAccount, UserConfig.getInstance(selectedAccount).unacceptedTermsOfService);
        }
    }
}
