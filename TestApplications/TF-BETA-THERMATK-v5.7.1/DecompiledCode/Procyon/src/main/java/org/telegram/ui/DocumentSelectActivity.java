// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.content.res.Configuration;
import java.util.List;
import java.util.Collections;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import org.telegram.ui.Cells.GraySectionCell;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ActionBarMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.FrameLayout;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.view.View$OnTouchListener;
import org.telegram.ui.ActionBar.Theme;
import java.util.Iterator;
import java.util.Collection;
import org.telegram.ui.Cells.SharedDocumentCell;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.BackDrawable;
import android.content.IntentFilter;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.annotation.SuppressLint;
import java.util.Comparator;
import java.util.Arrays;
import android.os.Environment;
import android.view.MotionEvent;
import org.telegram.messenger.LocaleController;
import android.os.StatFs;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;
import android.content.Intent;
import android.content.Context;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Components.NumberTextView;
import java.util.HashMap;
import android.content.BroadcastReceiver;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.Components.EmptyTextProgressView;
import java.io.File;
import android.view.View;
import java.util.ArrayList;
import org.telegram.ui.ActionBar.BaseFragment;

public class DocumentSelectActivity extends BaseFragment
{
    private static final int done = 3;
    private ArrayList<View> actionModeViews;
    private boolean allowMusic;
    private boolean canSelectOnlyImageFiles;
    private File currentDir;
    private DocumentSelectActivityDelegate delegate;
    private EmptyTextProgressView emptyView;
    private ArrayList<HistoryEntry> history;
    private ArrayList<ListItem> items;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int maxSelectedFiles;
    private BroadcastReceiver receiver;
    private boolean receiverRegistered;
    private ArrayList<ListItem> recentItems;
    private boolean scrolling;
    private HashMap<String, ListItem> selectedFiles;
    private NumberTextView selectedMessagesCountTextView;
    private long sizeLimit;
    
    public DocumentSelectActivity(final boolean allowMusic) {
        this.items = new ArrayList<ListItem>();
        this.receiverRegistered = false;
        this.history = new ArrayList<HistoryEntry>();
        this.sizeLimit = 1610612736L;
        this.selectedFiles = new HashMap<String, ListItem>();
        this.actionModeViews = new ArrayList<View>();
        this.recentItems = new ArrayList<ListItem>();
        this.maxSelectedFiles = -1;
        this.receiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final _$$Lambda$DocumentSelectActivity$1$QUHI_wOoyDp1Dwx1fGWii_dVi9g $$Lambda$DocumentSelectActivity$1$QUHI_wOoyDp1Dwx1fGWii_dVi9g = new _$$Lambda$DocumentSelectActivity$1$QUHI_wOoyDp1Dwx1fGWii_dVi9g(this);
                if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
                    DocumentSelectActivity.this.listView.postDelayed((Runnable)$$Lambda$DocumentSelectActivity$1$QUHI_wOoyDp1Dwx1fGWii_dVi9g, 1000L);
                }
                else {
                    $$Lambda$DocumentSelectActivity$1$QUHI_wOoyDp1Dwx1fGWii_dVi9g.run();
                }
            }
        };
        this.allowMusic = allowMusic;
    }
    
    private void fixLayoutInternal() {
        if (this.selectedMessagesCountTextView == null) {
            return;
        }
        if (!AndroidUtilities.isTablet() && ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == 2) {
            this.selectedMessagesCountTextView.setTextSize(18);
        }
        else {
            this.selectedMessagesCountTextView.setTextSize(20);
        }
    }
    
    private String getRootSubtitle(final String s) {
        try {
            final StatFs statFs = new StatFs(s);
            final long n = statFs.getBlockCount() * (long)statFs.getBlockSize();
            final long n2 = statFs.getAvailableBlocks();
            final long n3 = statFs.getBlockSize();
            if (n == 0L) {
                return "";
            }
            return LocaleController.formatString("FreeOfTotal", 2131559573, AndroidUtilities.formatFileSize(n2 * n3), AndroidUtilities.formatFileSize(n));
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return s;
        }
    }
    
    private boolean listFiles(final File file) {
        if (!file.canRead()) {
            if ((file.getAbsolutePath().startsWith(Environment.getExternalStorageDirectory().toString()) || file.getAbsolutePath().startsWith("/sdcard") || file.getAbsolutePath().startsWith("/mnt/sdcard")) && !Environment.getExternalStorageState().equals("mounted") && !Environment.getExternalStorageState().equals("mounted_ro")) {
                this.currentDir = file;
                this.items.clear();
                if ("shared".equals(Environment.getExternalStorageState())) {
                    this.emptyView.setText(LocaleController.getString("UsbActive", 2131560966));
                }
                else {
                    this.emptyView.setText(LocaleController.getString("NotMounted", 2131559960));
                }
                AndroidUtilities.clearDrawableAnimation((View)this.listView);
                this.scrolling = true;
                this.listAdapter.notifyDataSetChanged();
                return true;
            }
            this.showErrorBox(LocaleController.getString("AccessError", 2131558485));
            return false;
        }
        else {
            try {
                final File[] listFiles = file.listFiles();
                if (listFiles == null) {
                    this.showErrorBox(LocaleController.getString("UnknownError", 2131560937));
                    return false;
                }
                this.currentDir = file;
                this.items.clear();
                Arrays.sort(listFiles, (Comparator<? super File>)_$$Lambda$DocumentSelectActivity$KoBsEgVmPEJw_tYKCZKJ_jB7oso.INSTANCE);
                for (int i = 0; i < listFiles.length; ++i) {
                    final File file2 = listFiles[i];
                    if (file2.getName().indexOf(46) != 0) {
                        final ListItem e = new ListItem();
                        e.title = file2.getName();
                        e.file = file2;
                        if (file2.isDirectory()) {
                            e.icon = 2131165438;
                            e.subtitle = LocaleController.getString("Folder", 2131559497);
                        }
                        else {
                            final String name = file2.getName();
                            final String[] split = name.split("\\.");
                            String ext;
                            if (split.length > 1) {
                                ext = split[split.length - 1];
                            }
                            else {
                                ext = "?";
                            }
                            e.ext = ext;
                            e.subtitle = AndroidUtilities.formatFileSize(file2.length());
                            final String lowerCase = name.toLowerCase();
                            if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".png") || lowerCase.endsWith(".gif") || lowerCase.endsWith(".jpeg")) {
                                e.thumb = file2.getAbsolutePath();
                            }
                        }
                        this.items.add(e);
                    }
                }
                final ListItem element = new ListItem();
                element.title = "..";
                if (this.history.size() > 0) {
                    final ArrayList<HistoryEntry> history = this.history;
                    final File dir = history.get(history.size() - 1).dir;
                    if (dir == null) {
                        element.subtitle = LocaleController.getString("Folder", 2131559497);
                    }
                    else {
                        element.subtitle = dir.toString();
                    }
                }
                else {
                    element.subtitle = LocaleController.getString("Folder", 2131559497);
                }
                element.icon = 2131165438;
                element.file = null;
                this.items.add(0, element);
                AndroidUtilities.clearDrawableAnimation((View)this.listView);
                this.scrolling = true;
                this.listAdapter.notifyDataSetChanged();
                return true;
            }
            catch (Exception ex) {
                this.showErrorBox(ex.getLocalizedMessage());
                return false;
            }
        }
    }
    
    @SuppressLint({ "NewApi" })
    private void listRoots() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aconst_null    
        //     2: putfield        org/telegram/ui/DocumentSelectActivity.currentDir:Ljava/io/File;
        //     5: aload_0        
        //     6: getfield        org/telegram/ui/DocumentSelectActivity.items:Ljava/util/ArrayList;
        //     9: invokevirtual   java/util/ArrayList.clear:()V
        //    12: new             Ljava/util/HashSet;
        //    15: dup            
        //    16: invokespecial   java/util/HashSet.<init>:()V
        //    19: astore_1       
        //    20: invokestatic    android/os/Environment.getExternalStorageDirectory:()Ljava/io/File;
        //    23: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //    26: astore_2       
        //    27: invokestatic    android/os/Environment.isExternalStorageRemovable:()Z
        //    30: pop            
        //    31: invokestatic    android/os/Environment.getExternalStorageState:()Ljava/lang/String;
        //    34: astore_3       
        //    35: aload_3        
        //    36: ldc_w           "mounted"
        //    39: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    42: ifne            55
        //    45: aload_3        
        //    46: ldc_w           "mounted_ro"
        //    49: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    52: ifeq            145
        //    55: new             Lorg/telegram/ui/DocumentSelectActivity$ListItem;
        //    58: dup            
        //    59: aload_0        
        //    60: aconst_null    
        //    61: invokespecial   org/telegram/ui/DocumentSelectActivity$ListItem.<init>:(Lorg/telegram/ui/DocumentSelectActivity;Lorg/telegram/ui/DocumentSelectActivity$1;)V
        //    64: astore_3       
        //    65: invokestatic    android/os/Environment.isExternalStorageRemovable:()Z
        //    68: ifeq            94
        //    71: aload_3        
        //    72: ldc_w           "SdCard"
        //    75: ldc_w           2131560639
        //    78: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //    81: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.title:Ljava/lang/String;
        //    84: aload_3        
        //    85: ldc_w           2131165440
        //    88: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.icon:I
        //    91: goto            114
        //    94: aload_3        
        //    95: ldc_w           "InternalStorage"
        //    98: ldc_w           2131559670
        //   101: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   104: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.title:Ljava/lang/String;
        //   107: aload_3        
        //   108: ldc_w           2131165472
        //   111: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.icon:I
        //   114: aload_3        
        //   115: aload_0        
        //   116: aload_2        
        //   117: invokespecial   org/telegram/ui/DocumentSelectActivity.getRootSubtitle:(Ljava/lang/String;)Ljava/lang/String;
        //   120: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.subtitle:Ljava/lang/String;
        //   123: aload_3        
        //   124: invokestatic    android/os/Environment.getExternalStorageDirectory:()Ljava/io/File;
        //   127: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.file:Ljava/io/File;
        //   130: aload_0        
        //   131: getfield        org/telegram/ui/DocumentSelectActivity.items:Ljava/util/ArrayList;
        //   134: aload_3        
        //   135: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   138: pop            
        //   139: aload_1        
        //   140: aload_2        
        //   141: invokevirtual   java/util/HashSet.add:(Ljava/lang/Object;)Z
        //   144: pop            
        //   145: new             Ljava/io/BufferedReader;
        //   148: astore_3       
        //   149: new             Ljava/io/FileReader;
        //   152: astore_2       
        //   153: aload_2        
        //   154: ldc_w           "/proc/mounts"
        //   157: invokespecial   java/io/FileReader.<init>:(Ljava/lang/String;)V
        //   160: aload_3        
        //   161: aload_2        
        //   162: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   165: aload_3        
        //   166: astore_2       
        //   167: aload_3        
        //   168: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   171: astore          4
        //   173: aload           4
        //   175: ifnull          625
        //   178: aload_3        
        //   179: astore_2       
        //   180: aload           4
        //   182: ldc_w           "vfat"
        //   185: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   188: ifne            204
        //   191: aload_3        
        //   192: astore_2       
        //   193: aload           4
        //   195: ldc_w           "/mnt"
        //   198: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   201: ifeq            165
        //   204: aload_3        
        //   205: astore_2       
        //   206: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //   209: ifeq            219
        //   212: aload_3        
        //   213: astore_2       
        //   214: aload           4
        //   216: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //   219: aload_3        
        //   220: astore_2       
        //   221: new             Ljava/util/StringTokenizer;
        //   224: astore          5
        //   226: aload_3        
        //   227: astore_2       
        //   228: aload           5
        //   230: aload           4
        //   232: ldc_w           " "
        //   235: invokespecial   java/util/StringTokenizer.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   238: aload_3        
        //   239: astore_2       
        //   240: aload           5
        //   242: invokevirtual   java/util/StringTokenizer.nextToken:()Ljava/lang/String;
        //   245: pop            
        //   246: aload_3        
        //   247: astore_2       
        //   248: aload           5
        //   250: invokevirtual   java/util/StringTokenizer.nextToken:()Ljava/lang/String;
        //   253: astore          5
        //   255: aload_3        
        //   256: astore_2       
        //   257: aload_1        
        //   258: aload           5
        //   260: invokevirtual   java/util/HashSet.contains:(Ljava/lang/Object;)Z
        //   263: ifeq            269
        //   266: goto            165
        //   269: aload_3        
        //   270: astore_2       
        //   271: aload           4
        //   273: ldc_w           "/dev/block/vold"
        //   276: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   279: ifeq            165
        //   282: aload_3        
        //   283: astore_2       
        //   284: aload           4
        //   286: ldc_w           "/mnt/secure"
        //   289: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   292: ifne            165
        //   295: aload_3        
        //   296: astore_2       
        //   297: aload           4
        //   299: ldc_w           "/mnt/asec"
        //   302: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   305: ifne            165
        //   308: aload_3        
        //   309: astore_2       
        //   310: aload           4
        //   312: ldc_w           "/mnt/obb"
        //   315: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   318: ifne            165
        //   321: aload_3        
        //   322: astore_2       
        //   323: aload           4
        //   325: ldc_w           "/dev/mapper"
        //   328: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   331: ifne            165
        //   334: aload_3        
        //   335: astore_2       
        //   336: aload           4
        //   338: ldc_w           "tmpfs"
        //   341: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   344: ifne            165
        //   347: aload_3        
        //   348: astore_2       
        //   349: new             Ljava/io/File;
        //   352: astore          4
        //   354: aload_3        
        //   355: astore_2       
        //   356: aload           4
        //   358: aload           5
        //   360: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   363: aload_3        
        //   364: astore_2       
        //   365: aload           4
        //   367: invokevirtual   java/io/File.isDirectory:()Z
        //   370: ifne            474
        //   373: aload_3        
        //   374: astore_2       
        //   375: aload           5
        //   377: bipush          47
        //   379: invokevirtual   java/lang/String.lastIndexOf:(I)I
        //   382: istore          6
        //   384: iload           6
        //   386: iconst_m1      
        //   387: if_icmpeq       474
        //   390: aload_3        
        //   391: astore_2       
        //   392: new             Ljava/lang/StringBuilder;
        //   395: astore          4
        //   397: aload_3        
        //   398: astore_2       
        //   399: aload           4
        //   401: invokespecial   java/lang/StringBuilder.<init>:()V
        //   404: aload_3        
        //   405: astore_2       
        //   406: aload           4
        //   408: ldc_w           "/storage/"
        //   411: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   414: pop            
        //   415: aload_3        
        //   416: astore_2       
        //   417: aload           4
        //   419: aload           5
        //   421: iload           6
        //   423: iconst_1       
        //   424: iadd           
        //   425: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   428: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   431: pop            
        //   432: aload_3        
        //   433: astore_2       
        //   434: aload           4
        //   436: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   439: astore          4
        //   441: aload_3        
        //   442: astore_2       
        //   443: new             Ljava/io/File;
        //   446: astore          7
        //   448: aload_3        
        //   449: astore_2       
        //   450: aload           7
        //   452: aload           4
        //   454: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   457: aload_3        
        //   458: astore_2       
        //   459: aload           7
        //   461: invokevirtual   java/io/File.isDirectory:()Z
        //   464: ifeq            474
        //   467: aload           4
        //   469: astore          5
        //   471: goto            474
        //   474: aload_3        
        //   475: astore_2       
        //   476: aload_1        
        //   477: aload           5
        //   479: invokevirtual   java/util/HashSet.add:(Ljava/lang/Object;)Z
        //   482: pop            
        //   483: aload_3        
        //   484: astore_2       
        //   485: new             Lorg/telegram/ui/DocumentSelectActivity$ListItem;
        //   488: astore          4
        //   490: aload_3        
        //   491: astore_2       
        //   492: aload           4
        //   494: aload_0        
        //   495: aconst_null    
        //   496: invokespecial   org/telegram/ui/DocumentSelectActivity$ListItem.<init>:(Lorg/telegram/ui/DocumentSelectActivity;Lorg/telegram/ui/DocumentSelectActivity$1;)V
        //   499: aload_3        
        //   500: astore_2       
        //   501: aload           5
        //   503: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   506: ldc_w           "sd"
        //   509: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   512: ifeq            534
        //   515: aload_3        
        //   516: astore_2       
        //   517: aload           4
        //   519: ldc_w           "SdCard"
        //   522: ldc_w           2131560639
        //   525: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   528: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.title:Ljava/lang/String;
        //   531: goto            550
        //   534: aload_3        
        //   535: astore_2       
        //   536: aload           4
        //   538: ldc_w           "ExternalStorage"
        //   541: ldc_w           2131559476
        //   544: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   547: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.title:Ljava/lang/String;
        //   550: aload_3        
        //   551: astore_2       
        //   552: aload           4
        //   554: ldc_w           2131165440
        //   557: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.icon:I
        //   560: aload_3        
        //   561: astore_2       
        //   562: aload           4
        //   564: aload_0        
        //   565: aload           5
        //   567: invokespecial   org/telegram/ui/DocumentSelectActivity.getRootSubtitle:(Ljava/lang/String;)Ljava/lang/String;
        //   570: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.subtitle:Ljava/lang/String;
        //   573: aload_3        
        //   574: astore_2       
        //   575: new             Ljava/io/File;
        //   578: astore          7
        //   580: aload_3        
        //   581: astore_2       
        //   582: aload           7
        //   584: aload           5
        //   586: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   589: aload_3        
        //   590: astore_2       
        //   591: aload           4
        //   593: aload           7
        //   595: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.file:Ljava/io/File;
        //   598: aload_3        
        //   599: astore_2       
        //   600: aload_0        
        //   601: getfield        org/telegram/ui/DocumentSelectActivity.items:Ljava/util/ArrayList;
        //   604: aload           4
        //   606: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   609: pop            
        //   610: goto            165
        //   613: astore          5
        //   615: aload_3        
        //   616: astore_2       
        //   617: aload           5
        //   619: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   622: goto            165
        //   625: aload_3        
        //   626: invokevirtual   java/io/BufferedReader.close:()V
        //   629: goto            670
        //   632: astore          5
        //   634: goto            647
        //   637: astore_2       
        //   638: aconst_null    
        //   639: astore_3       
        //   640: goto            953
        //   643: astore          5
        //   645: aconst_null    
        //   646: astore_3       
        //   647: aload_3        
        //   648: astore_2       
        //   649: aload           5
        //   651: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   654: aload_3        
        //   655: ifnull          670
        //   658: aload_3        
        //   659: invokevirtual   java/io/BufferedReader.close:()V
        //   662: goto            670
        //   665: astore_2       
        //   666: aload_2        
        //   667: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   670: new             Lorg/telegram/ui/DocumentSelectActivity$ListItem;
        //   673: dup            
        //   674: aload_0        
        //   675: aconst_null    
        //   676: invokespecial   org/telegram/ui/DocumentSelectActivity$ListItem.<init>:(Lorg/telegram/ui/DocumentSelectActivity;Lorg/telegram/ui/DocumentSelectActivity$1;)V
        //   679: astore_2       
        //   680: aload_2        
        //   681: ldc_w           "/"
        //   684: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.title:Ljava/lang/String;
        //   687: aload_2        
        //   688: ldc_w           "SystemRoot"
        //   691: ldc_w           2131560857
        //   694: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   697: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.subtitle:Ljava/lang/String;
        //   700: aload_2        
        //   701: ldc_w           2131165438
        //   704: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.icon:I
        //   707: aload_2        
        //   708: new             Ljava/io/File;
        //   711: dup            
        //   712: ldc_w           "/"
        //   715: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   718: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.file:Ljava/io/File;
        //   721: aload_0        
        //   722: getfield        org/telegram/ui/DocumentSelectActivity.items:Ljava/util/ArrayList;
        //   725: aload_2        
        //   726: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   729: pop            
        //   730: new             Ljava/io/File;
        //   733: astore_2       
        //   734: aload_2        
        //   735: invokestatic    android/os/Environment.getExternalStorageDirectory:()Ljava/io/File;
        //   738: ldc_w           "Telegram"
        //   741: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   744: aload_2        
        //   745: invokevirtual   java/io/File.exists:()Z
        //   748: ifeq            805
        //   751: new             Lorg/telegram/ui/DocumentSelectActivity$ListItem;
        //   754: astore_3       
        //   755: aload_3        
        //   756: aload_0        
        //   757: aconst_null    
        //   758: invokespecial   org/telegram/ui/DocumentSelectActivity$ListItem.<init>:(Lorg/telegram/ui/DocumentSelectActivity;Lorg/telegram/ui/DocumentSelectActivity$1;)V
        //   761: aload_3        
        //   762: ldc_w           "Telegram"
        //   765: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.title:Ljava/lang/String;
        //   768: aload_3        
        //   769: aload_2        
        //   770: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //   773: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.subtitle:Ljava/lang/String;
        //   776: aload_3        
        //   777: ldc_w           2131165438
        //   780: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.icon:I
        //   783: aload_3        
        //   784: aload_2        
        //   785: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.file:Ljava/io/File;
        //   788: aload_0        
        //   789: getfield        org/telegram/ui/DocumentSelectActivity.items:Ljava/util/ArrayList;
        //   792: aload_3        
        //   793: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   796: pop            
        //   797: goto            805
        //   800: astore_2       
        //   801: aload_2        
        //   802: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   805: new             Lorg/telegram/ui/DocumentSelectActivity$ListItem;
        //   808: dup            
        //   809: aload_0        
        //   810: aconst_null    
        //   811: invokespecial   org/telegram/ui/DocumentSelectActivity$ListItem.<init>:(Lorg/telegram/ui/DocumentSelectActivity;Lorg/telegram/ui/DocumentSelectActivity$1;)V
        //   814: astore_2       
        //   815: aload_2        
        //   816: ldc_w           "Gallery"
        //   819: ldc_w           2131559585
        //   822: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   825: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.title:Ljava/lang/String;
        //   828: aload_2        
        //   829: ldc_w           "GalleryInfo"
        //   832: ldc_w           2131559586
        //   835: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   838: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.subtitle:Ljava/lang/String;
        //   841: aload_2        
        //   842: ldc_w           2131165473
        //   845: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.icon:I
        //   848: aload_2        
        //   849: aconst_null    
        //   850: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.file:Ljava/io/File;
        //   853: aload_0        
        //   854: getfield        org/telegram/ui/DocumentSelectActivity.items:Ljava/util/ArrayList;
        //   857: aload_2        
        //   858: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   861: pop            
        //   862: aload_0        
        //   863: getfield        org/telegram/ui/DocumentSelectActivity.allowMusic:Z
        //   866: ifeq            926
        //   869: new             Lorg/telegram/ui/DocumentSelectActivity$ListItem;
        //   872: dup            
        //   873: aload_0        
        //   874: aconst_null    
        //   875: invokespecial   org/telegram/ui/DocumentSelectActivity$ListItem.<init>:(Lorg/telegram/ui/DocumentSelectActivity;Lorg/telegram/ui/DocumentSelectActivity$1;)V
        //   878: astore_2       
        //   879: aload_2        
        //   880: ldc_w           "AttachMusic"
        //   883: ldc_w           2131558726
        //   886: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   889: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.title:Ljava/lang/String;
        //   892: aload_2        
        //   893: ldc_w           "MusicInfo"
        //   896: ldc_w           2131559884
        //   899: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   902: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.subtitle:Ljava/lang/String;
        //   905: aload_2        
        //   906: ldc_w           2131165474
        //   909: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.icon:I
        //   912: aload_2        
        //   913: aconst_null    
        //   914: putfield        org/telegram/ui/DocumentSelectActivity$ListItem.file:Ljava/io/File;
        //   917: aload_0        
        //   918: getfield        org/telegram/ui/DocumentSelectActivity.items:Ljava/util/ArrayList;
        //   921: aload_2        
        //   922: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   925: pop            
        //   926: aload_0        
        //   927: getfield        org/telegram/ui/DocumentSelectActivity.listView:Lorg/telegram/ui/Components/RecyclerListView;
        //   930: invokestatic    org/telegram/messenger/AndroidUtilities.clearDrawableAnimation:(Landroid/view/View;)V
        //   933: aload_0        
        //   934: iconst_1       
        //   935: putfield        org/telegram/ui/DocumentSelectActivity.scrolling:Z
        //   938: aload_0        
        //   939: getfield        org/telegram/ui/DocumentSelectActivity.listAdapter:Lorg/telegram/ui/DocumentSelectActivity$ListAdapter;
        //   942: invokevirtual   androidx/recyclerview/widget/RecyclerView$Adapter.notifyDataSetChanged:()V
        //   945: return         
        //   946: astore          5
        //   948: aload_2        
        //   949: astore_3       
        //   950: aload           5
        //   952: astore_2       
        //   953: aload_3        
        //   954: ifnull          969
        //   957: aload_3        
        //   958: invokevirtual   java/io/BufferedReader.close:()V
        //   961: goto            969
        //   964: astore_3       
        //   965: aload_3        
        //   966: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   969: aload_2        
        //   970: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  145    165    643    647    Ljava/lang/Exception;
        //  145    165    637    643    Any
        //  167    173    632    637    Ljava/lang/Exception;
        //  167    173    946    953    Any
        //  180    191    632    637    Ljava/lang/Exception;
        //  180    191    946    953    Any
        //  193    204    632    637    Ljava/lang/Exception;
        //  193    204    946    953    Any
        //  206    212    632    637    Ljava/lang/Exception;
        //  206    212    946    953    Any
        //  214    219    632    637    Ljava/lang/Exception;
        //  214    219    946    953    Any
        //  221    226    632    637    Ljava/lang/Exception;
        //  221    226    946    953    Any
        //  228    238    632    637    Ljava/lang/Exception;
        //  228    238    946    953    Any
        //  240    246    632    637    Ljava/lang/Exception;
        //  240    246    946    953    Any
        //  248    255    632    637    Ljava/lang/Exception;
        //  248    255    946    953    Any
        //  257    266    632    637    Ljava/lang/Exception;
        //  257    266    946    953    Any
        //  271    282    632    637    Ljava/lang/Exception;
        //  271    282    946    953    Any
        //  284    295    632    637    Ljava/lang/Exception;
        //  284    295    946    953    Any
        //  297    308    632    637    Ljava/lang/Exception;
        //  297    308    946    953    Any
        //  310    321    632    637    Ljava/lang/Exception;
        //  310    321    946    953    Any
        //  323    334    632    637    Ljava/lang/Exception;
        //  323    334    946    953    Any
        //  336    347    632    637    Ljava/lang/Exception;
        //  336    347    946    953    Any
        //  349    354    632    637    Ljava/lang/Exception;
        //  349    354    946    953    Any
        //  356    363    632    637    Ljava/lang/Exception;
        //  356    363    946    953    Any
        //  365    373    632    637    Ljava/lang/Exception;
        //  365    373    946    953    Any
        //  375    384    632    637    Ljava/lang/Exception;
        //  375    384    946    953    Any
        //  392    397    632    637    Ljava/lang/Exception;
        //  392    397    946    953    Any
        //  399    404    632    637    Ljava/lang/Exception;
        //  399    404    946    953    Any
        //  406    415    632    637    Ljava/lang/Exception;
        //  406    415    946    953    Any
        //  417    432    632    637    Ljava/lang/Exception;
        //  417    432    946    953    Any
        //  434    441    632    637    Ljava/lang/Exception;
        //  434    441    946    953    Any
        //  443    448    632    637    Ljava/lang/Exception;
        //  443    448    946    953    Any
        //  450    457    632    637    Ljava/lang/Exception;
        //  450    457    946    953    Any
        //  459    467    632    637    Ljava/lang/Exception;
        //  459    467    946    953    Any
        //  476    483    632    637    Ljava/lang/Exception;
        //  476    483    946    953    Any
        //  485    490    613    625    Ljava/lang/Exception;
        //  485    490    946    953    Any
        //  492    499    613    625    Ljava/lang/Exception;
        //  492    499    946    953    Any
        //  501    515    613    625    Ljava/lang/Exception;
        //  501    515    946    953    Any
        //  517    531    613    625    Ljava/lang/Exception;
        //  517    531    946    953    Any
        //  536    550    613    625    Ljava/lang/Exception;
        //  536    550    946    953    Any
        //  552    560    613    625    Ljava/lang/Exception;
        //  552    560    946    953    Any
        //  562    573    613    625    Ljava/lang/Exception;
        //  562    573    946    953    Any
        //  575    580    613    625    Ljava/lang/Exception;
        //  575    580    946    953    Any
        //  582    589    613    625    Ljava/lang/Exception;
        //  582    589    946    953    Any
        //  591    598    613    625    Ljava/lang/Exception;
        //  591    598    946    953    Any
        //  600    610    613    625    Ljava/lang/Exception;
        //  600    610    946    953    Any
        //  617    622    632    637    Ljava/lang/Exception;
        //  617    622    946    953    Any
        //  625    629    665    670    Ljava/lang/Exception;
        //  649    654    946    953    Any
        //  658    662    665    670    Ljava/lang/Exception;
        //  730    797    800    805    Ljava/lang/Exception;
        //  957    961    964    969    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0204:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    private void showErrorBox(final String message) {
        if (this.getParentActivity() == null) {
            return;
        }
        new AlertDialog.Builder((Context)this.getParentActivity()).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(message).setPositiveButton(LocaleController.getString("OK", 2131560097), null).show();
    }
    
    @Override
    public View createView(final Context context) {
        if (!this.receiverRegistered) {
            this.receiverRegistered = true;
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
            intentFilter.addAction("android.intent.action.MEDIA_CHECKING");
            intentFilter.addAction("android.intent.action.MEDIA_EJECT");
            intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
            intentFilter.addAction("android.intent.action.MEDIA_NOFS");
            intentFilter.addAction("android.intent.action.MEDIA_REMOVED");
            intentFilter.addAction("android.intent.action.MEDIA_SHARED");
            intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
            intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
            intentFilter.addDataScheme("file");
            ApplicationLoader.applicationContext.registerReceiver(this.receiver, intentFilter);
        }
        super.actionBar.setBackButtonDrawable(new BackDrawable(false));
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("SelectFile", 2131560682));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    if (DocumentSelectActivity.this.actionBar.isActionModeShowed()) {
                        DocumentSelectActivity.this.selectedFiles.clear();
                        DocumentSelectActivity.this.actionBar.hideActionMode();
                        int childCount;
                        View child;
                        for (childCount = DocumentSelectActivity.this.listView.getChildCount(), i = 0; i < childCount; ++i) {
                            child = DocumentSelectActivity.this.listView.getChildAt(i);
                            if (child instanceof SharedDocumentCell) {
                                ((SharedDocumentCell)child).setChecked(false, true);
                            }
                        }
                    }
                    else {
                        DocumentSelectActivity.this.finishFragment();
                    }
                }
                else if (i == 3 && DocumentSelectActivity.this.delegate != null) {
                    DocumentSelectActivity.this.delegate.didSelectFiles(DocumentSelectActivity.this, new ArrayList<String>(DocumentSelectActivity.this.selectedFiles.keySet()));
                    final Iterator<ListItem> iterator = DocumentSelectActivity.this.selectedFiles.values().iterator();
                    while (iterator.hasNext()) {
                        iterator.next().date = System.currentTimeMillis();
                    }
                }
            }
        });
        this.selectedFiles.clear();
        this.actionModeViews.clear();
        final ActionBarMenu actionMode = super.actionBar.createActionMode();
        (this.selectedMessagesCountTextView = new NumberTextView(actionMode.getContext())).setTextSize(18);
        this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.selectedMessagesCountTextView.setTextColor(Theme.getColor("actionBarActionModeDefaultIcon"));
        this.selectedMessagesCountTextView.setOnTouchListener((View$OnTouchListener)_$$Lambda$DocumentSelectActivity$wOAW0__TUXtkjQ_Z0vg8d4FmxpE.INSTANCE);
        actionMode.addView((View)this.selectedMessagesCountTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, 1.0f, 65, 0, 0, 0));
        this.actionModeViews.add((View)actionMode.addItemWithWidth(3, 2131165412, AndroidUtilities.dp(54.0f)));
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.emptyView = new EmptyTextProgressView(context)).showTextView();
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        this.listView.setEmptyView((View)this.emptyView);
        this.listView.setAdapter(this.listAdapter = new ListAdapter(context));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                DocumentSelectActivity.this.scrolling = (n != 0);
            }
        });
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$DocumentSelectActivity$CyKwYdaVewVAaKBAnA3V_bH1l5E(this));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$DocumentSelectActivity$lBQknjeUipxjA7znufbDrc42LRU(this));
        this.listRoots();
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_AM_ITEMSCOLOR, null, null, null, null, "actionBarActionModeDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_AM_BACKGROUND, null, null, null, null, "actionBarActionModeDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_AM_TOPBACKGROUND, null, null, null, null, "actionBarActionModeDefaultTop"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, null, null, null, null, "actionBarActionModeDefaultSelector"), new ThemeDescription(this.selectedMessagesCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "actionBarActionModeDefaultIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { GraySectionCell.class }, new String[] { "textView" }, null, null, null, "key_graySectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { GraySectionCell.class }, null, null, null, "graySection"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { SharedDocumentCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { SharedDocumentCell.class }, new String[] { "dateTextView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[] { SharedDocumentCell.class }, new String[] { "checkBox" }, null, null, null, "checkbox"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { SharedDocumentCell.class }, new String[] { "checkBox" }, null, null, null, "checkboxCheck"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { SharedDocumentCell.class }, new String[] { "thumbImageView" }, null, null, null, "files_folderIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { SharedDocumentCell.class }, new String[] { "thumbImageView" }, null, null, null, "files_folderIconBackground"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { SharedDocumentCell.class }, new String[] { "extTextView" }, null, null, null, "files_iconText") };
    }
    
    public void loadRecentFiles() {
        try {
            final File[] listFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles();
            for (int i = 0; i < listFiles.length; ++i) {
                final File file = listFiles[i];
                if (!file.isDirectory()) {
                    final ListItem e = new ListItem();
                    e.title = file.getName();
                    e.file = file;
                    final String name = file.getName();
                    final String[] split = name.split("\\.");
                    String ext;
                    if (split.length > 1) {
                        ext = split[split.length - 1];
                    }
                    else {
                        ext = "?";
                    }
                    e.ext = ext;
                    e.subtitle = AndroidUtilities.formatFileSize(file.length());
                    final String lowerCase = name.toLowerCase();
                    if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".png") || lowerCase.endsWith(".gif") || lowerCase.endsWith(".jpeg")) {
                        e.thumb = file.getAbsolutePath();
                    }
                    this.recentItems.add(e);
                }
            }
            Collections.sort(this.recentItems, (Comparator<? super ListItem>)_$$Lambda$DocumentSelectActivity$9fKm6qZG_Rnisjsmtz1XWQccmXA.INSTANCE);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    @Override
    public boolean onBackPressed() {
        if (this.history.size() > 0) {
            final ArrayList<HistoryEntry> history = this.history;
            final HistoryEntry historyEntry = history.remove(history.size() - 1);
            super.actionBar.setTitle(historyEntry.title);
            final File dir = historyEntry.dir;
            if (dir != null) {
                this.listFiles(dir);
            }
            else {
                this.listRoots();
            }
            this.layoutManager.scrollToPositionWithOffset(historyEntry.scrollItem, historyEntry.scrollOffset);
            return false;
        }
        return super.onBackPressed();
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            listView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    DocumentSelectActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                    DocumentSelectActivity.this.fixLayoutInternal();
                    return true;
                }
            });
        }
    }
    
    @Override
    public boolean onFragmentCreate() {
        this.loadRecentFiles();
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        try {
            if (this.receiverRegistered) {
                ApplicationLoader.applicationContext.unregisterReceiver(this.receiver);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        super.onFragmentDestroy();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
        this.fixLayoutInternal();
    }
    
    public void setCanSelectOnlyImageFiles(final boolean b) {
        this.canSelectOnlyImageFiles = true;
    }
    
    public void setDelegate(final DocumentSelectActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setMaxSelectedFiles(final int maxSelectedFiles) {
        this.maxSelectedFiles = maxSelectedFiles;
    }
    
    public interface DocumentSelectActivityDelegate
    {
        void didSelectFiles(final DocumentSelectActivity p0, final ArrayList<String> p1);
        
        void startDocumentSelectActivity();
        
        void startMusicSelectActivity(final BaseFragment p0);
    }
    
    private class HistoryEntry
    {
        File dir;
        int scrollItem;
        int scrollOffset;
        String title;
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        public ListItem getItem(int n) {
            if (n < DocumentSelectActivity.this.items.size()) {
                return DocumentSelectActivity.this.items.get(n);
            }
            if (DocumentSelectActivity.this.history.isEmpty() && !DocumentSelectActivity.this.recentItems.isEmpty() && n != DocumentSelectActivity.this.items.size()) {
                n -= DocumentSelectActivity.this.items.size() + 1;
                if (n < DocumentSelectActivity.this.recentItems.size()) {
                    return DocumentSelectActivity.this.recentItems.get(n);
                }
            }
            return null;
        }
        
        @Override
        public int getItemCount() {
            int size;
            final int n = size = DocumentSelectActivity.this.items.size();
            if (DocumentSelectActivity.this.history.isEmpty()) {
                size = n;
                if (!DocumentSelectActivity.this.recentItems.isEmpty()) {
                    size = n + (DocumentSelectActivity.this.recentItems.size() + 1);
                }
            }
            return size;
        }
        
        @Override
        public int getItemViewType(int n) {
            if (this.getItem(n) != null) {
                n = 1;
            }
            else {
                n = 0;
            }
            return n;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int icon) {
            if (viewHolder.getItemViewType() == 1) {
                final ListItem item = this.getItem(icon);
                final SharedDocumentCell sharedDocumentCell = (SharedDocumentCell)viewHolder.itemView;
                icon = item.icon;
                if (icon != 0) {
                    sharedDocumentCell.setTextAndValueAndTypeAndThumb(item.title, item.subtitle, null, null, icon);
                }
                else {
                    sharedDocumentCell.setTextAndValueAndTypeAndThumb(item.title, item.subtitle, item.ext.toUpperCase().substring(0, Math.min(item.ext.length(), 4)), item.thumb, 0);
                }
                if (item.file != null && DocumentSelectActivity.this.actionBar.isActionModeShowed()) {
                    sharedDocumentCell.setChecked(DocumentSelectActivity.this.selectedFiles.containsKey(item.file.toString()), DocumentSelectActivity.this.scrolling ^ true);
                }
                else {
                    sharedDocumentCell.setChecked(false, DocumentSelectActivity.this.scrolling ^ true);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                frameLayout = new SharedDocumentCell(this.mContext);
            }
            else {
                frameLayout = new GraySectionCell(this.mContext);
                ((GraySectionCell)frameLayout).setText(LocaleController.getString("Recent", 2131560537));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
    
    private class ListItem
    {
        long date;
        String ext;
        File file;
        int icon;
        String subtitle;
        String thumb;
        String title;
        
        private ListItem() {
            this.subtitle = "";
            this.ext = "";
        }
    }
}
