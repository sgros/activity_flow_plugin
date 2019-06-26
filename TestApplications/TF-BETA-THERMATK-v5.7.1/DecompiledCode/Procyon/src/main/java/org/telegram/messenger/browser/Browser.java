// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.browser;

import android.os.Bundle;
import android.content.DialogInterface$OnCancelListener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.UserConfig;
import android.content.DialogInterface;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.AlertDialog;
import android.net.Uri;
import org.telegram.messenger.support.customtabs.CustomTabsCallback;
import org.telegram.messenger.support.customtabsclient.shared.ServiceConnection;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.support.customtabsclient.shared.ServiceConnectionCallback;
import android.content.Context;
import org.telegram.messenger.support.customtabsclient.shared.CustomTabsHelper;
import android.text.TextUtils;
import org.telegram.messenger.support.customtabs.CustomTabsServiceConnection;
import org.telegram.messenger.support.customtabs.CustomTabsSession;
import org.telegram.messenger.support.customtabs.CustomTabsClient;
import android.app.Activity;
import java.lang.ref.WeakReference;

public class Browser
{
    private static WeakReference<Activity> currentCustomTabsActivity;
    private static CustomTabsClient customTabsClient;
    private static WeakReference<CustomTabsSession> customTabsCurrentSession;
    private static String customTabsPackageToBind;
    private static CustomTabsServiceConnection customTabsServiceConnection;
    private static CustomTabsSession customTabsSession;
    
    public static void bindCustomTabsService(final Activity referent) {
        final WeakReference<Activity> currentCustomTabsActivity = Browser.currentCustomTabsActivity;
        Activity activity;
        if (currentCustomTabsActivity == null) {
            activity = null;
        }
        else {
            activity = currentCustomTabsActivity.get();
        }
        if (activity != null && activity != referent) {
            unbindCustomTabsService(activity);
        }
        if (Browser.customTabsClient != null) {
            return;
        }
        Browser.currentCustomTabsActivity = new WeakReference<Activity>(referent);
        try {
            if (TextUtils.isEmpty((CharSequence)Browser.customTabsPackageToBind)) {
                Browser.customTabsPackageToBind = CustomTabsHelper.getPackageNameToUse((Context)referent);
                if (Browser.customTabsPackageToBind == null) {
                    return;
                }
            }
            Browser.customTabsServiceConnection = new ServiceConnection(new ServiceConnectionCallback() {
                @Override
                public void onServiceConnected(final CustomTabsClient customTabsClient) {
                    Browser.customTabsClient = customTabsClient;
                    if (SharedConfig.customTabs && Browser.customTabsClient != null) {
                        try {
                            Browser.customTabsClient.warmup(0L);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                }
                
                @Override
                public void onServiceDisconnected() {
                    Browser.customTabsClient = null;
                }
            });
            if (!CustomTabsClient.bindCustomTabsService((Context)referent, Browser.customTabsPackageToBind, Browser.customTabsServiceConnection)) {
                Browser.customTabsServiceConnection = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private static CustomTabsSession getCurrentSession() {
        final WeakReference<CustomTabsSession> customTabsCurrentSession = Browser.customTabsCurrentSession;
        CustomTabsSession customTabsSession;
        if (customTabsCurrentSession == null) {
            customTabsSession = null;
        }
        else {
            customTabsSession = customTabsCurrentSession.get();
        }
        return customTabsSession;
    }
    
    private static CustomTabsSession getSession() {
        final CustomTabsClient customTabsClient = Browser.customTabsClient;
        if (customTabsClient == null) {
            Browser.customTabsSession = null;
        }
        else if (Browser.customTabsSession == null) {
            setCurrentSession(Browser.customTabsSession = customTabsClient.newSession(new NavigationCallback()));
        }
        return Browser.customTabsSession;
    }
    
    public static boolean isInternalUri(final Uri uri, final boolean[] array) {
        final String host = uri.getHost();
        String lowerCase;
        if (host != null) {
            lowerCase = host.toLowerCase();
        }
        else {
            lowerCase = "";
        }
        if ("tg".equals(uri.getScheme())) {
            return true;
        }
        if ("telegram.dog".equals(lowerCase)) {
            final String path = uri.getPath();
            if (path != null && path.length() > 1) {
                final String lowerCase2 = path.substring(1).toLowerCase();
                if (!lowerCase2.startsWith("blog") && !lowerCase2.equals("iv") && !lowerCase2.startsWith("faq") && !lowerCase2.equals("apps") && !lowerCase2.startsWith("s/")) {
                    return true;
                }
                if (array != null) {
                    array[0] = true;
                }
                return false;
            }
        }
        else if ("telegram.me".equals(lowerCase) || "t.me".equals(lowerCase)) {
            final String path2 = uri.getPath();
            if (path2 != null && path2.length() > 1) {
                final String lowerCase3 = path2.substring(1).toLowerCase();
                if (!lowerCase3.equals("iv") && !lowerCase3.startsWith("s/")) {
                    return true;
                }
                if (array != null) {
                    array[0] = true;
                }
            }
        }
        return false;
    }
    
    public static boolean isInternalUrl(final String s, final boolean[] array) {
        return isInternalUri(Uri.parse(s), array);
    }
    
    public static boolean isPassportUrl(String lowerCase) {
        if (lowerCase == null) {
            return false;
        }
        try {
            lowerCase = lowerCase.toLowerCase();
            if (lowerCase.startsWith("tg:passport") || lowerCase.startsWith("tg://passport") || lowerCase.startsWith("tg:secureid") || (lowerCase.contains("resolve") && lowerCase.contains("domain=telegrampassport"))) {
                return true;
            }
            return false;
        }
        catch (Throwable t) {
            return false;
        }
    }
    
    public static void openUrl(final Context context, final Uri uri) {
        openUrl(context, uri, true);
    }
    
    public static void openUrl(final Context context, final Uri uri, final boolean b) {
        openUrl(context, uri, b, true);
    }
    
    public static void openUrl(final Context p0, final Uri p1, final boolean p2, final boolean p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          4
        //     3: aload_0        
        //     4: ifnull          1198
        //     7: aload           4
        //     9: ifnonnull       15
        //    12: goto            1198
        //    15: getstatic       org/telegram/messenger/UserConfig.selectedAccount:I
        //    18: istore          5
        //    20: iconst_1       
        //    21: newarray        Z
        //    23: astore          6
        //    25: aload           6
        //    27: iconst_0       
        //    28: iconst_0       
        //    29: bastore        
        //    30: aload           4
        //    32: aload           6
        //    34: invokestatic    org/telegram/messenger/browser/Browser.isInternalUri:(Landroid/net/Uri;[Z)Z
        //    37: istore          7
        //    39: iload_3        
        //    40: ifeq            177
        //    43: aload_1        
        //    44: invokevirtual   android/net/Uri.getHost:()Ljava/lang/String;
        //    47: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //    50: ldc_w           "telegra.ph"
        //    53: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    56: ifne            75
        //    59: aload_1        
        //    60: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //    63: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //    66: ldc_w           "telegram.org/faq"
        //    69: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //    72: ifeq            177
        //    75: iconst_1       
        //    76: anewarray       Lorg/telegram/ui/ActionBar/AlertDialog;
        //    79: astore          8
        //    81: new             Lorg/telegram/ui/ActionBar/AlertDialog;
        //    84: astore          9
        //    86: aload           9
        //    88: aload_0        
        //    89: iconst_3       
        //    90: invokespecial   org/telegram/ui/ActionBar/AlertDialog.<init>:(Landroid/content/Context;I)V
        //    93: aload           8
        //    95: iconst_0       
        //    96: aload           9
        //    98: aastore        
        //    99: new             Lorg/telegram/tgnet/TLRPC$TL_messages_getWebPagePreview;
        //   102: astore          10
        //   104: aload           10
        //   106: invokespecial   org/telegram/tgnet/TLRPC$TL_messages_getWebPagePreview.<init>:()V
        //   109: aload           10
        //   111: aload_1        
        //   112: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //   115: putfield        org/telegram/tgnet/TLRPC$TL_messages_getWebPagePreview.message:Ljava/lang/String;
        //   118: getstatic       org/telegram/messenger/UserConfig.selectedAccount:I
        //   121: invokestatic    org/telegram/tgnet/ConnectionsManager.getInstance:(I)Lorg/telegram/tgnet/ConnectionsManager;
        //   124: astore          11
        //   126: new             Lorg/telegram/messenger/browser/_$$Lambda$Browser$UGNHp8wusapZ_BRGXGaD_Ayz4_I;
        //   129: astore          9
        //   131: aload           9
        //   133: aload           8
        //   135: iload           5
        //   137: aload_1        
        //   138: aload_0        
        //   139: iload_2        
        //   140: invokespecial   org/telegram/messenger/browser/_$$Lambda$Browser$UGNHp8wusapZ_BRGXGaD_Ayz4_I.<init>:([Lorg/telegram/ui/ActionBar/AlertDialog;ILandroid/net/Uri;Landroid/content/Context;Z)V
        //   143: aload           11
        //   145: aload           10
        //   147: aload           9
        //   149: invokevirtual   org/telegram/tgnet/ConnectionsManager.sendRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/tgnet/RequestDelegate;)I
        //   152: istore          5
        //   154: new             Lorg/telegram/messenger/browser/_$$Lambda$Browser$tzkgx_C1l2oH_szh01yqocI6uLg;
        //   157: astore          9
        //   159: aload           9
        //   161: aload           8
        //   163: iload           5
        //   165: invokespecial   org/telegram/messenger/browser/_$$Lambda$Browser$tzkgx_C1l2oH_szh01yqocI6uLg.<init>:([Lorg/telegram/ui/ActionBar/AlertDialog;I)V
        //   168: aload           9
        //   170: ldc2_w          1000
        //   173: invokestatic    org/telegram/messenger/AndroidUtilities.runOnUIThread:(Ljava/lang/Runnable;J)V
        //   176: return         
        //   177: aload           4
        //   179: astore          9
        //   181: aload_1        
        //   182: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
        //   185: ifnull          204
        //   188: aload           4
        //   190: astore          9
        //   192: aload_1        
        //   193: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
        //   196: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   199: astore          10
        //   201: goto            208
        //   204: ldc             ""
        //   206: astore          10
        //   208: aload           4
        //   210: astore          9
        //   212: ldc_w           "http"
        //   215: aload           10
        //   217: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   220: ifne            244
        //   223: aload           4
        //   225: astore          9
        //   227: ldc_w           "https"
        //   230: aload           10
        //   232: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   235: istore_3       
        //   236: aload           4
        //   238: astore          8
        //   240: iload_3        
        //   241: ifeq            266
        //   244: aload_1        
        //   245: invokevirtual   android/net/Uri.normalizeScheme:()Landroid/net/Uri;
        //   248: astore          8
        //   250: goto            266
        //   253: astore_1       
        //   254: aload           4
        //   256: astore          9
        //   258: aload_1        
        //   259: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   262: aload           4
        //   264: astore          8
        //   266: aload           8
        //   268: astore_1       
        //   269: iload_2        
        //   270: ifeq            1117
        //   273: aload           8
        //   275: astore          9
        //   277: aload           8
        //   279: astore_1       
        //   280: getstatic       org/telegram/messenger/SharedConfig.customTabs:Z
        //   283: ifeq            1117
        //   286: aload           8
        //   288: astore_1       
        //   289: iload           7
        //   291: ifne            1117
        //   294: aload           8
        //   296: astore          9
        //   298: aload           10
        //   300: ldc_w           "tel"
        //   303: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   306: istore_2       
        //   307: aload           8
        //   309: astore_1       
        //   310: iload_2        
        //   311: ifne            1117
        //   314: aconst_null    
        //   315: astore          4
        //   317: new             Landroid/content/Intent;
        //   320: astore_1       
        //   321: aload_1        
        //   322: ldc_w           "android.intent.action.VIEW"
        //   325: ldc_w           "http://www.google.com"
        //   328: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //   331: invokespecial   android/content/Intent.<init>:(Ljava/lang/String;Landroid/net/Uri;)V
        //   334: aload_0        
        //   335: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //   338: aload_1        
        //   339: iconst_0       
        //   340: invokevirtual   android/content/pm/PackageManager.queryIntentActivities:(Landroid/content/Intent;I)Ljava/util/List;
        //   343: astore          10
        //   345: aload           10
        //   347: ifnull          460
        //   350: aload           10
        //   352: invokeinterface java/util/List.isEmpty:()Z
        //   357: ifne            460
        //   360: aload           10
        //   362: invokeinterface java/util/List.size:()I
        //   367: anewarray       Ljava/lang/String;
        //   370: astore_1       
        //   371: iconst_0       
        //   372: istore          5
        //   374: aload_1        
        //   375: astore          9
        //   377: iload           5
        //   379: aload           10
        //   381: invokeinterface java/util/List.size:()I
        //   386: if_icmpge       463
        //   389: aload_1        
        //   390: iload           5
        //   392: aload           10
        //   394: iload           5
        //   396: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   401: checkcast       Landroid/content/pm/ResolveInfo;
        //   404: getfield        android/content/pm/ResolveInfo.activityInfo:Landroid/content/pm/ActivityInfo;
        //   407: getfield        android/content/pm/ActivityInfo.packageName:Ljava/lang/String;
        //   410: aastore        
        //   411: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //   414: ifeq            454
        //   417: new             Ljava/lang/StringBuilder;
        //   420: astore          9
        //   422: aload           9
        //   424: invokespecial   java/lang/StringBuilder.<init>:()V
        //   427: aload           9
        //   429: ldc_w           "default browser name = "
        //   432: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   435: pop            
        //   436: aload           9
        //   438: aload_1        
        //   439: iload           5
        //   441: aaload         
        //   442: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   445: pop            
        //   446: aload           9
        //   448: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   451: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //   454: iinc            5, 1
        //   457: goto            374
        //   460: aconst_null    
        //   461: astore          9
        //   463: aload           4
        //   465: astore_1       
        //   466: new             Landroid/content/Intent;
        //   469: astore          10
        //   471: aload           4
        //   473: astore_1       
        //   474: aload           10
        //   476: ldc_w           "android.intent.action.VIEW"
        //   479: aload           8
        //   481: invokespecial   android/content/Intent.<init>:(Ljava/lang/String;Landroid/net/Uri;)V
        //   484: aload           4
        //   486: astore_1       
        //   487: aload_0        
        //   488: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //   491: aload           10
        //   493: iconst_0       
        //   494: invokevirtual   android/content/pm/PackageManager.queryIntentActivities:(Landroid/content/Intent;I)Ljava/util/List;
        //   497: astore          4
        //   499: aload           9
        //   501: ifnull          609
        //   504: iconst_0       
        //   505: istore          5
        //   507: aload           4
        //   509: astore_1       
        //   510: iload           5
        //   512: aload           4
        //   514: invokeinterface java/util/List.size:()I
        //   519: if_icmpge       725
        //   522: iconst_0       
        //   523: istore          12
        //   525: iload           5
        //   527: istore          13
        //   529: aload           4
        //   531: astore_1       
        //   532: iload           12
        //   534: aload           9
        //   536: arraylength    
        //   537: if_icmpge       600
        //   540: aload           4
        //   542: astore_1       
        //   543: aload           9
        //   545: iload           12
        //   547: aaload         
        //   548: aload           4
        //   550: iload           5
        //   552: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   557: checkcast       Landroid/content/pm/ResolveInfo;
        //   560: getfield        android/content/pm/ResolveInfo.activityInfo:Landroid/content/pm/ActivityInfo;
        //   563: getfield        android/content/pm/ActivityInfo.packageName:Ljava/lang/String;
        //   566: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   569: ifeq            594
        //   572: aload           4
        //   574: astore_1       
        //   575: aload           4
        //   577: iload           5
        //   579: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
        //   584: pop            
        //   585: iload           5
        //   587: iconst_1       
        //   588: isub           
        //   589: istore          13
        //   591: goto            600
        //   594: iinc            12, 1
        //   597: goto            525
        //   600: iload           13
        //   602: iconst_1       
        //   603: iadd           
        //   604: istore          5
        //   606: goto            507
        //   609: iconst_0       
        //   610: istore          5
        //   612: aload           4
        //   614: astore_1       
        //   615: iload           5
        //   617: aload           4
        //   619: invokeinterface java/util/List.size:()I
        //   624: if_icmpge       725
        //   627: aload           4
        //   629: astore_1       
        //   630: aload           4
        //   632: iload           5
        //   634: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   639: checkcast       Landroid/content/pm/ResolveInfo;
        //   642: getfield        android/content/pm/ResolveInfo.activityInfo:Landroid/content/pm/ActivityInfo;
        //   645: getfield        android/content/pm/ActivityInfo.packageName:Ljava/lang/String;
        //   648: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   651: ldc_w           "browser"
        //   654: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   657: ifne            697
        //   660: iload           5
        //   662: istore          12
        //   664: aload           4
        //   666: astore_1       
        //   667: aload           4
        //   669: iload           5
        //   671: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   676: checkcast       Landroid/content/pm/ResolveInfo;
        //   679: getfield        android/content/pm/ResolveInfo.activityInfo:Landroid/content/pm/ActivityInfo;
        //   682: getfield        android/content/pm/ActivityInfo.packageName:Ljava/lang/String;
        //   685: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   688: ldc_w           "chrome"
        //   691: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   694: ifeq            716
        //   697: aload           4
        //   699: astore_1       
        //   700: aload           4
        //   702: iload           5
        //   704: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
        //   709: pop            
        //   710: iload           5
        //   712: iconst_1       
        //   713: isub           
        //   714: istore          12
        //   716: iload           12
        //   718: iconst_1       
        //   719: iadd           
        //   720: istore          5
        //   722: goto            612
        //   725: aload           4
        //   727: astore          10
        //   729: aload           4
        //   731: astore_1       
        //   732: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //   735: ifeq            858
        //   738: iconst_0       
        //   739: istore          5
        //   741: aload           4
        //   743: astore          10
        //   745: aload           4
        //   747: astore_1       
        //   748: iload           5
        //   750: aload           4
        //   752: invokeinterface java/util/List.size:()I
        //   757: if_icmpge       858
        //   760: aload           4
        //   762: astore_1       
        //   763: new             Ljava/lang/StringBuilder;
        //   766: astore          9
        //   768: aload           4
        //   770: astore_1       
        //   771: aload           9
        //   773: invokespecial   java/lang/StringBuilder.<init>:()V
        //   776: aload           4
        //   778: astore_1       
        //   779: aload           9
        //   781: ldc_w           "device has "
        //   784: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   787: pop            
        //   788: aload           4
        //   790: astore_1       
        //   791: aload           9
        //   793: aload           4
        //   795: iload           5
        //   797: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   802: checkcast       Landroid/content/pm/ResolveInfo;
        //   805: getfield        android/content/pm/ResolveInfo.activityInfo:Landroid/content/pm/ActivityInfo;
        //   808: getfield        android/content/pm/ActivityInfo.packageName:Ljava/lang/String;
        //   811: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   814: pop            
        //   815: aload           4
        //   817: astore_1       
        //   818: aload           9
        //   820: ldc_w           " to open "
        //   823: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   826: pop            
        //   827: aload           4
        //   829: astore_1       
        //   830: aload           9
        //   832: aload           8
        //   834: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //   837: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   840: pop            
        //   841: aload           4
        //   843: astore_1       
        //   844: aload           9
        //   846: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   849: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //   852: iinc            5, 1
        //   855: goto            741
        //   858: aload           6
        //   860: iconst_0       
        //   861: baload         
        //   862: ifne            887
        //   865: aload           10
        //   867: ifnull          887
        //   870: aload           8
        //   872: astore          9
        //   874: aload           8
        //   876: astore_1       
        //   877: aload           10
        //   879: invokeinterface java/util/List.isEmpty:()Z
        //   884: ifeq            1117
        //   887: aload           8
        //   889: astore          9
        //   891: new             Landroid/content/Intent;
        //   894: astore_1       
        //   895: aload           8
        //   897: astore          9
        //   899: aload_1        
        //   900: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   903: ldc_w           Lorg/telegram/messenger/ShareBroadcastReceiver;.class
        //   906: invokespecial   android/content/Intent.<init>:(Landroid/content/Context;Ljava/lang/Class;)V
        //   909: aload           8
        //   911: astore          9
        //   913: aload_1        
        //   914: ldc_w           "android.intent.action.SEND"
        //   917: invokevirtual   android/content/Intent.setAction:(Ljava/lang/String;)Landroid/content/Intent;
        //   920: pop            
        //   921: aload           8
        //   923: astore          9
        //   925: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   928: astore          4
        //   930: aload           8
        //   932: astore          9
        //   934: new             Landroid/content/Intent;
        //   937: astore          10
        //   939: aload           8
        //   941: astore          9
        //   943: aload           10
        //   945: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   948: ldc_w           Lorg/telegram/messenger/CustomTabsCopyReceiver;.class
        //   951: invokespecial   android/content/Intent.<init>:(Landroid/content/Context;Ljava/lang/Class;)V
        //   954: aload           8
        //   956: astore          9
        //   958: aload           4
        //   960: iconst_0       
        //   961: aload           10
        //   963: ldc_w           134217728
        //   966: invokestatic    android/app/PendingIntent.getBroadcast:(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
        //   969: astore          10
        //   971: aload           8
        //   973: astore          9
        //   975: new             Lorg/telegram/messenger/support/customtabs/CustomTabsIntent$Builder;
        //   978: astore          4
        //   980: aload           8
        //   982: astore          9
        //   984: aload           4
        //   986: invokestatic    org/telegram/messenger/browser/Browser.getSession:()Lorg/telegram/messenger/support/customtabs/CustomTabsSession;
        //   989: invokespecial   org/telegram/messenger/support/customtabs/CustomTabsIntent$Builder.<init>:(Lorg/telegram/messenger/support/customtabs/CustomTabsSession;)V
        //   992: aload           8
        //   994: astore          9
        //   996: aload           4
        //   998: ldc_w           "CopyLink"
        //  1001: ldc_w           2131559164
        //  1004: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  1007: aload           10
        //  1009: invokevirtual   org/telegram/messenger/support/customtabs/CustomTabsIntent$Builder.addMenuItem:(Ljava/lang/String;Landroid/app/PendingIntent;)Lorg/telegram/messenger/support/customtabs/CustomTabsIntent$Builder;
        //  1012: pop            
        //  1013: aload           8
        //  1015: astore          9
        //  1017: aload           4
        //  1019: ldc_w           "actionBarDefault"
        //  1022: invokestatic    org/telegram/ui/ActionBar/Theme.getColor:(Ljava/lang/String;)I
        //  1025: invokevirtual   org/telegram/messenger/support/customtabs/CustomTabsIntent$Builder.setToolbarColor:(I)Lorg/telegram/messenger/support/customtabs/CustomTabsIntent$Builder;
        //  1028: pop            
        //  1029: aload           8
        //  1031: astore          9
        //  1033: aload           4
        //  1035: iconst_1       
        //  1036: invokevirtual   org/telegram/messenger/support/customtabs/CustomTabsIntent$Builder.setShowTitle:(Z)Lorg/telegram/messenger/support/customtabs/CustomTabsIntent$Builder;
        //  1039: pop            
        //  1040: aload           8
        //  1042: astore          9
        //  1044: aload           4
        //  1046: aload_0        
        //  1047: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //  1050: ldc_w           2131165214
        //  1053: invokestatic    android/graphics/BitmapFactory.decodeResource:(Landroid/content/res/Resources;I)Landroid/graphics/Bitmap;
        //  1056: ldc_w           "ShareFile"
        //  1059: ldc_w           2131560748
        //  1062: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  1065: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  1068: iconst_0       
        //  1069: aload_1        
        //  1070: iconst_0       
        //  1071: invokestatic    android/app/PendingIntent.getBroadcast:(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
        //  1074: iconst_0       
        //  1075: invokevirtual   org/telegram/messenger/support/customtabs/CustomTabsIntent$Builder.setActionButton:(Landroid/graphics/Bitmap;Ljava/lang/String;Landroid/app/PendingIntent;Z)Lorg/telegram/messenger/support/customtabs/CustomTabsIntent$Builder;
        //  1078: pop            
        //  1079: aload           8
        //  1081: astore          9
        //  1083: aload           4
        //  1085: invokevirtual   org/telegram/messenger/support/customtabs/CustomTabsIntent$Builder.build:()Lorg/telegram/messenger/support/customtabs/CustomTabsIntent;
        //  1088: astore_1       
        //  1089: aload           8
        //  1091: astore          9
        //  1093: aload_1        
        //  1094: invokevirtual   org/telegram/messenger/support/customtabs/CustomTabsIntent.setUseNewTask:()V
        //  1097: aload           8
        //  1099: astore          9
        //  1101: aload_1        
        //  1102: aload_0        
        //  1103: aload           8
        //  1105: invokevirtual   org/telegram/messenger/support/customtabs/CustomTabsIntent.launchUrl:(Landroid/content/Context;Landroid/net/Uri;)V
        //  1108: return         
        //  1109: astore_1       
        //  1110: aload_1        
        //  1111: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1114: aload           9
        //  1116: astore_1       
        //  1117: new             Landroid/content/Intent;
        //  1120: astore          8
        //  1122: aload           8
        //  1124: ldc_w           "android.intent.action.VIEW"
        //  1127: aload_1        
        //  1128: invokespecial   android/content/Intent.<init>:(Ljava/lang/String;Landroid/net/Uri;)V
        //  1131: iload           7
        //  1133: ifeq            1161
        //  1136: new             Landroid/content/ComponentName;
        //  1139: astore_1       
        //  1140: aload_1        
        //  1141: aload_0        
        //  1142: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  1145: ldc_w           Lorg/telegram/ui/LaunchActivity;.class
        //  1148: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //  1151: invokespecial   android/content/ComponentName.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1154: aload           8
        //  1156: aload_1        
        //  1157: invokevirtual   android/content/Intent.setComponent:(Landroid/content/ComponentName;)Landroid/content/Intent;
        //  1160: pop            
        //  1161: aload           8
        //  1163: ldc_w           "create_new_tab"
        //  1166: iconst_1       
        //  1167: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Z)Landroid/content/Intent;
        //  1170: pop            
        //  1171: aload           8
        //  1173: ldc_w           "com.android.browser.application_id"
        //  1176: aload_0        
        //  1177: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  1180: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
        //  1183: pop            
        //  1184: aload_0        
        //  1185: aload           8
        //  1187: invokevirtual   android/content/Context.startActivity:(Landroid/content/Intent;)V
        //  1190: goto            1198
        //  1193: astore_0       
        //  1194: aload_0        
        //  1195: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1198: return         
        //  1199: astore          8
        //  1201: goto            177
        //  1204: astore_1       
        //  1205: goto            460
        //  1208: astore          9
        //  1210: aload_1        
        //  1211: astore          9
        //  1213: goto            463
        //  1216: astore          9
        //  1218: aload_1        
        //  1219: astore          10
        //  1221: goto            858
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  43     75     1199   1204   Ljava/lang/Exception;
        //  75     93     1199   1204   Ljava/lang/Exception;
        //  99     176    1199   1204   Ljava/lang/Exception;
        //  181    188    1109   1117   Ljava/lang/Exception;
        //  192    201    1109   1117   Ljava/lang/Exception;
        //  212    223    1109   1117   Ljava/lang/Exception;
        //  227    236    1109   1117   Ljava/lang/Exception;
        //  244    250    253    266    Ljava/lang/Exception;
        //  258    262    1109   1117   Ljava/lang/Exception;
        //  280    286    1109   1117   Ljava/lang/Exception;
        //  298    307    1109   1117   Ljava/lang/Exception;
        //  317    345    1204   1208   Ljava/lang/Exception;
        //  350    371    1204   1208   Ljava/lang/Exception;
        //  377    454    1208   1216   Ljava/lang/Exception;
        //  466    471    1216   1224   Ljava/lang/Exception;
        //  474    484    1216   1224   Ljava/lang/Exception;
        //  487    499    1216   1224   Ljava/lang/Exception;
        //  510    522    1216   1224   Ljava/lang/Exception;
        //  532    540    1216   1224   Ljava/lang/Exception;
        //  543    572    1216   1224   Ljava/lang/Exception;
        //  575    585    1216   1224   Ljava/lang/Exception;
        //  615    627    1216   1224   Ljava/lang/Exception;
        //  630    660    1216   1224   Ljava/lang/Exception;
        //  667    697    1216   1224   Ljava/lang/Exception;
        //  700    710    1216   1224   Ljava/lang/Exception;
        //  732    738    1216   1224   Ljava/lang/Exception;
        //  748    760    1216   1224   Ljava/lang/Exception;
        //  763    768    1216   1224   Ljava/lang/Exception;
        //  771    776    1216   1224   Ljava/lang/Exception;
        //  779    788    1216   1224   Ljava/lang/Exception;
        //  791    815    1216   1224   Ljava/lang/Exception;
        //  818    827    1216   1224   Ljava/lang/Exception;
        //  830    841    1216   1224   Ljava/lang/Exception;
        //  844    852    1216   1224   Ljava/lang/Exception;
        //  877    887    1109   1117   Ljava/lang/Exception;
        //  891    895    1109   1117   Ljava/lang/Exception;
        //  899    909    1109   1117   Ljava/lang/Exception;
        //  913    921    1109   1117   Ljava/lang/Exception;
        //  925    930    1109   1117   Ljava/lang/Exception;
        //  934    939    1109   1117   Ljava/lang/Exception;
        //  943    954    1109   1117   Ljava/lang/Exception;
        //  958    971    1109   1117   Ljava/lang/Exception;
        //  975    980    1109   1117   Ljava/lang/Exception;
        //  984    992    1109   1117   Ljava/lang/Exception;
        //  996    1013   1109   1117   Ljava/lang/Exception;
        //  1017   1029   1109   1117   Ljava/lang/Exception;
        //  1033   1040   1109   1117   Ljava/lang/Exception;
        //  1044   1079   1109   1117   Ljava/lang/Exception;
        //  1083   1089   1109   1117   Ljava/lang/Exception;
        //  1093   1097   1109   1117   Ljava/lang/Exception;
        //  1101   1108   1109   1117   Ljava/lang/Exception;
        //  1117   1131   1193   1198   Ljava/lang/Exception;
        //  1136   1161   1193   1198   Ljava/lang/Exception;
        //  1161   1190   1193   1198   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 562 out-of-bounds for length 562
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
    
    public static void openUrl(final Context context, final String s) {
        if (s == null) {
            return;
        }
        openUrl(context, Uri.parse(s), true);
    }
    
    public static void openUrl(final Context context, final String s, final boolean b) {
        if (context != null) {
            if (s != null) {
                openUrl(context, Uri.parse(s), b);
            }
        }
    }
    
    public static void openUrl(final Context context, final String s, final boolean b, final boolean b2) {
        openUrl(context, Uri.parse(s), b, b2);
    }
    
    private static void setCurrentSession(final CustomTabsSession referent) {
        Browser.customTabsCurrentSession = new WeakReference<CustomTabsSession>(referent);
    }
    
    public static void unbindCustomTabsService(final Activity activity) {
        if (Browser.customTabsServiceConnection == null) {
            return;
        }
        final WeakReference<Activity> currentCustomTabsActivity = Browser.currentCustomTabsActivity;
        Activity activity2;
        if (currentCustomTabsActivity == null) {
            activity2 = null;
        }
        else {
            activity2 = currentCustomTabsActivity.get();
        }
        if (activity2 == activity) {
            Browser.currentCustomTabsActivity.clear();
        }
        while (true) {
            try {
                activity.unbindService((android.content.ServiceConnection)Browser.customTabsServiceConnection);
                Browser.customTabsClient = null;
                Browser.customTabsSession = null;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    private static class NavigationCallback extends CustomTabsCallback
    {
        @Override
        public void onNavigationEvent(final int n, final Bundle bundle) {
        }
    }
}
