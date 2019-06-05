// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity;

import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.preferences.Preferences;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.Menu;
import menion.android.whereyougo.utils.ManagerNotify;
import android.net.Uri;
import menion.android.whereyougo.network.activity.DownloadCartridgeActivity;
import android.os.Build$VERSION;
import android.os.Bundle;
import menion.android.whereyougo.VersionInfo;
import android.app.NotificationManager;
import android.view.View$OnLongClickListener;
import menion.android.whereyougo.gui.dialog.AboutDialog;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.TextView;
import java.io.FileOutputStream;
import menion.android.whereyougo.gui.dialog.ChooseSavegameDialog;
import menion.android.whereyougo.utils.A;
import cz.matejcik.openwig.platform.LocationService;
import cz.matejcik.openwig.platform.UI;
import cz.matejcik.openwig.Engine;
import java.io.OutputStream;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import java.io.IOException;
import menion.android.whereyougo.utils.Logger;
import java.io.File;
import android.support.v4.app.Fragment;
import menion.android.whereyougo.gui.dialog.ChooseCartridgeDialog;
import menion.android.whereyougo.maps.utils.MapDataProvider;
import cz.matejcik.openwig.EventTable;
import menion.android.whereyougo.maps.utils.MapHelper;
import android.annotation.TargetApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.openwig.WUI;
import menion.android.whereyougo.openwig.WLocationService;
import java.util.Vector;
import cz.matejcik.openwig.formats.CartridgeFile;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;

public class MainActivity extends CustomMainActivity
{
    private static final String TAG = "Main";
    public static CartridgeFile cartridgeFile;
    private static Vector<CartridgeFile> cartridgeFiles;
    public static String selectedFile;
    private static final WLocationService wLocationService;
    public static final WUI wui;
    
    static {
        wui = new WUI();
        wLocationService = new WLocationService();
        MainActivity.wui.setOnSavingStarted(new Runnable() {
            @Override
            public void run() {
                try {
                    FileSystem.backupFile(MainActivity.getSaveFile());
                }
                catch (Exception ex) {}
            }
        });
    }
    
    public static boolean callGudingScreen(final Activity activity) {
        activity.startActivity(new Intent((Context)activity, (Class)GuidingActivity.class));
        return true;
    }
    
    @TargetApi(23)
    private void checkPermissions() {
        final String[] array = { "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE" };
        for (int length = array.length, i = 0; i < length; ++i) {
            if (ContextCompat.checkSelfPermission((Context)this, array[i]) != 0) {
                ActivityCompat.requestPermissions(this, array, 0);
                break;
            }
        }
    }
    
    private void clickMap() {
        final MapDataProvider mapDataProvider = MapHelper.getMapDataProvider();
        mapDataProvider.clear();
        mapDataProvider.addCartridges(MainActivity.cartridgeFiles);
        MainActivity.wui.showScreen(14, null);
    }
    
    private void clickStart() {
        if (this.isAnyCartridgeAvailable()) {
            final ChooseCartridgeDialog chooseCartridgeDialog = new ChooseCartridgeDialog();
            chooseCartridgeDialog.setParams(MainActivity.cartridgeFiles);
            this.getSupportFragmentManager().beginTransaction().add(chooseCartridgeDialog, "DIALOG_TAG_CHOOSE_CARTRIDGE").commitAllowingStateLoss();
        }
    }
    
    public static File getLogFile() throws IOException {
        try {
            return new File(MainActivity.selectedFile.substring(0, MainActivity.selectedFile.length() - 3) + "owl");
        }
        catch (SecurityException ex) {
            Logger.e("Main", "getSyncFile()", ex);
            return null;
        }
    }
    
    public static File getSaveFile() throws IOException {
        try {
            return new File(MainActivity.selectedFile.substring(0, MainActivity.selectedFile.length() - 3) + "ows");
        }
        catch (SecurityException ex) {
            Logger.e("Main", "getSyncFile()", ex);
            return null;
        }
    }
    
    public static String getSelectedFile() {
        return MainActivity.selectedFile;
    }
    
    private boolean isAnyCartridgeAvailable() {
        boolean b = true;
        if (MainActivity.cartridgeFiles == null || MainActivity.cartridgeFiles.size() == 0) {
            UtilsGUI.showDialogInfo(this, this.getString(2131165346, new Object[] { FileSystem.ROOT, "WhereYouGo" }));
            b = false;
        }
        return b;
    }
    
    private static void loadCartridge(final OutputStream outputStream) {
        try {
            WUI.startProgressDialog();
            Engine.newInstance(MainActivity.cartridgeFile, outputStream, MainActivity.wui, MainActivity.wLocationService).start();
        }
        catch (Throwable t) {}
    }
    
    public static void openCartridge(final CartridgeFile cartridgeFile) {
        final CustomMainActivity main = A.getMain();
        if (main != null) {
            try {
                MainActivity.cartridgeFile = cartridgeFile;
                MainActivity.selectedFile = MainActivity.cartridgeFile.filename;
                main.getSupportFragmentManager().beginTransaction().add(ChooseSavegameDialog.newInstance(getSaveFile()), "DIALOG_TAG_CHOOSE_SAVE_FILE").commitAllowingStateLoss();
            }
            catch (Exception ex) {
                Logger.e("Main", "onCreate()", ex);
            }
        }
    }
    
    private void openCartridge(final File p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aload_2        
        //     3: astore_3       
        //     4: new             Lmenion/android/whereyougo/openwig/WSeekableFile;
        //     7: astore          4
        //     9: aload_2        
        //    10: astore_3       
        //    11: aload           4
        //    13: aload_1        
        //    14: invokespecial   menion/android/whereyougo/openwig/WSeekableFile.<init>:(Ljava/io/File;)V
        //    17: aload_2        
        //    18: astore_3       
        //    19: new             Lmenion/android/whereyougo/openwig/WSaveFile;
        //    22: astore          5
        //    24: aload_2        
        //    25: astore_3       
        //    26: aload           5
        //    28: aload_1        
        //    29: invokespecial   menion/android/whereyougo/openwig/WSaveFile.<init>:(Ljava/io/File;)V
        //    32: aload_2        
        //    33: astore_3       
        //    34: aload           4
        //    36: aload           5
        //    38: invokestatic    cz/matejcik/openwig/formats/CartridgeFile.read:(Lcz/matejcik/openwig/platform/SeekableFile;Lcz/matejcik/openwig/platform/FileHandle;)Lcz/matejcik/openwig/formats/CartridgeFile;
        //    41: astore_2       
        //    42: aload_2        
        //    43: ifnull          62
        //    46: aload_2        
        //    47: astore_3       
        //    48: aload_2        
        //    49: aload_1        
        //    50: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    53: putfield        cz/matejcik/openwig/formats/CartridgeFile.filename:Ljava/lang/String;
        //    56: aload_2        
        //    57: astore_3       
        //    58: aload_3        
        //    59: invokestatic    menion/android/whereyougo/gui/activity/MainActivity.openCartridge:(Lcz/matejcik/openwig/formats/CartridgeFile;)V
        //    62: return         
        //    63: astore_2       
        //    64: new             Ljava/lang/StringBuilder;
        //    67: astore          4
        //    69: aload           4
        //    71: invokespecial   java/lang/StringBuilder.<init>:()V
        //    74: ldc             "Main"
        //    76: aload           4
        //    78: ldc_w           "openCartridge(), file:"
        //    81: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    84: aload_1        
        //    85: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    88: ldc_w           ", e:"
        //    91: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    94: aload_2        
        //    95: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
        //    98: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   101: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   104: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   107: ldc_w           2131165214
        //   110: iconst_1       
        //   111: anewarray       Ljava/lang/Object;
        //   114: dup            
        //   115: iconst_0       
        //   116: aload_1        
        //   117: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   120: aastore        
        //   121: invokestatic    menion/android/whereyougo/preferences/Locale.getString:(I[Ljava/lang/Object;)Ljava/lang/String;
        //   124: invokestatic    menion/android/whereyougo/utils/ManagerNotify.toastShortMessage:(Ljava/lang/String;)V
        //   127: goto            58
        //   130: astore_1       
        //   131: ldc             "Main"
        //   133: ldc_w           "onCreate()"
        //   136: aload_1        
        //   137: invokestatic    menion/android/whereyougo/utils/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Exception;)V
        //   140: goto            62
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      9      63     130    Ljava/lang/Exception;
        //  11     17     63     130    Ljava/lang/Exception;
        //  19     24     63     130    Ljava/lang/Exception;
        //  26     32     63     130    Ljava/lang/Exception;
        //  34     42     63     130    Ljava/lang/Exception;
        //  48     56     63     130    Ljava/lang/Exception;
        //  58     62     130    143    Ljava/lang/Exception;
        //  64     127    130    143    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0058:
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
    
    public static void refreshCartridges() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/lang/StringBuilder.<init>:()V
        //     7: ldc_w           "refreshCartridges(), "
        //    10: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    13: astore_0       
        //    14: getstatic       menion/android/whereyougo/gui/activity/MainActivity.selectedFile:Ljava/lang/String;
        //    17: ifnonnull       206
        //    20: iconst_1       
        //    21: istore_1       
        //    22: ldc             "Main"
        //    24: aload_0        
        //    25: iload_1        
        //    26: invokevirtual   java/lang/StringBuilder.append:(Z)Ljava/lang/StringBuilder;
        //    29: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    32: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //    35: getstatic       menion/android/whereyougo/utils/FileSystem.ROOT:Ljava/lang/String;
        //    38: ldc_w           "gwc"
        //    41: invokestatic    menion/android/whereyougo/utils/FileSystem.getFiles:(Ljava/lang/String;Ljava/lang/String;)[Ljava/io/File;
        //    44: astore_0       
        //    45: new             Ljava/util/Vector;
        //    48: dup            
        //    49: invokespecial   java/util/Vector.<init>:()V
        //    52: putstatic       menion/android/whereyougo/gui/activity/MainActivity.cartridgeFiles:Ljava/util/Vector;
        //    55: new             Ljava/util/ArrayList;
        //    58: dup            
        //    59: invokespecial   java/util/ArrayList.<init>:()V
        //    62: astore_2       
        //    63: aload_0        
        //    64: ifnull          277
        //    67: aload_0        
        //    68: arraylength    
        //    69: istore_3       
        //    70: iconst_0       
        //    71: istore          4
        //    73: iload           4
        //    75: iload_3        
        //    76: if_icmpge       277
        //    79: aload_0        
        //    80: iload           4
        //    82: aaload         
        //    83: astore          5
        //    85: new             Lmenion/android/whereyougo/openwig/WSeekableFile;
        //    88: astore          6
        //    90: aload           6
        //    92: aload           5
        //    94: invokespecial   menion/android/whereyougo/openwig/WSeekableFile.<init>:(Ljava/io/File;)V
        //    97: new             Lmenion/android/whereyougo/openwig/WSaveFile;
        //   100: astore          7
        //   102: aload           7
        //   104: aload           5
        //   106: invokespecial   menion/android/whereyougo/openwig/WSaveFile.<init>:(Ljava/io/File;)V
        //   109: aload           6
        //   111: aload           7
        //   113: invokestatic    cz/matejcik/openwig/formats/CartridgeFile.read:(Lcz/matejcik/openwig/platform/SeekableFile;Lcz/matejcik/openwig/platform/FileHandle;)Lcz/matejcik/openwig/formats/CartridgeFile;
        //   116: astore          7
        //   118: aload           7
        //   120: ifnull          200
        //   123: aload           7
        //   125: aload           5
        //   127: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   130: putfield        cz/matejcik/openwig/formats/CartridgeFile.filename:Ljava/lang/String;
        //   133: new             Llocus/api/objects/extra/Location;
        //   136: astore          6
        //   138: aload           6
        //   140: ldc             "Main"
        //   142: invokespecial   locus/api/objects/extra/Location.<init>:(Ljava/lang/String;)V
        //   145: aload           6
        //   147: aload           7
        //   149: getfield        cz/matejcik/openwig/formats/CartridgeFile.latitude:D
        //   152: invokevirtual   locus/api/objects/extra/Location.setLatitude:(D)Llocus/api/objects/extra/Location;
        //   155: pop            
        //   156: aload           6
        //   158: aload           7
        //   160: getfield        cz/matejcik/openwig/formats/CartridgeFile.longitude:D
        //   163: invokevirtual   locus/api/objects/extra/Location.setLongitude:(D)Llocus/api/objects/extra/Location;
        //   166: pop            
        //   167: new             Llocus/api/objects/extra/Waypoint;
        //   170: astore          8
        //   172: aload           8
        //   174: aload           7
        //   176: getfield        cz/matejcik/openwig/formats/CartridgeFile.name:Ljava/lang/String;
        //   179: aload           6
        //   181: invokespecial   locus/api/objects/extra/Waypoint.<init>:(Ljava/lang/String;Llocus/api/objects/extra/Location;)V
        //   184: getstatic       menion/android/whereyougo/gui/activity/MainActivity.cartridgeFiles:Ljava/util/Vector;
        //   187: aload           7
        //   189: invokevirtual   java/util/Vector.add:(Ljava/lang/Object;)Z
        //   192: pop            
        //   193: aload_2        
        //   194: aload           8
        //   196: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   199: pop            
        //   200: iinc            4, 1
        //   203: goto            73
        //   206: iconst_0       
        //   207: istore_1       
        //   208: goto            22
        //   211: astore          7
        //   213: ldc             "Main"
        //   215: new             Ljava/lang/StringBuilder;
        //   218: dup            
        //   219: invokespecial   java/lang/StringBuilder.<init>:()V
        //   222: ldc_w           "refreshCartridge(), file:"
        //   225: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   228: aload           5
        //   230: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   233: ldc_w           ", e:"
        //   236: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   239: aload           7
        //   241: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
        //   244: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   247: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   250: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   253: ldc_w           2131165214
        //   256: iconst_1       
        //   257: anewarray       Ljava/lang/Object;
        //   260: dup            
        //   261: iconst_0       
        //   262: aload           5
        //   264: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   267: aastore        
        //   268: invokestatic    menion/android/whereyougo/preferences/Locale.getString:(I[Ljava/lang/Object;)Ljava/lang/String;
        //   271: invokestatic    menion/android/whereyougo/utils/ManagerNotify.toastShortMessage:(Ljava/lang/String;)V
        //   274: goto            200
        //   277: aload_2        
        //   278: invokevirtual   java/util/ArrayList.size:()I
        //   281: ifle            284
        //   284: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  85     118    211    277    Ljava/lang/Exception;
        //  123    200    211    277    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    private static void restoreCartridge(final OutputStream outputStream) {
        try {
            WUI.startProgressDialog();
            Engine.newInstance(MainActivity.cartridgeFile, outputStream, MainActivity.wui, MainActivity.wLocationService).restore();
        }
        catch (Throwable t) {}
    }
    
    public static void setSelectedFile(final String selectedFile) {
        MainActivity.selectedFile = selectedFile;
    }
    
    public static void startSelectedCartridge(final boolean b) {
        while (true) {
            OutputStream outputStream = null;
            try {
                final File logFile = getLogFile();
                outputStream = null;
                try {
                    if (!logFile.exists()) {
                        logFile.createNewFile();
                    }
                    outputStream = new FileOutputStream(logFile, true);
                    if (b) {
                        restoreCartridge(outputStream);
                        return;
                    }
                }
                catch (Exception ex) {
                    Logger.e("Main", "onResume() - create empty saveGame file", ex);
                }
            }
            catch (IOException outputStream) {
                ((Throwable)outputStream).printStackTrace();
                return;
            }
            loadCartridge(outputStream);
        }
    }
    
    @Override
    protected void eventCreateLayout() {
        this.setContentView(2130903055);
        ((TextView)this.findViewById(2131492866)).setText((CharSequence)"WhereYouGo");
        UtilsGUI.setButtons(this, new int[] { 2131492962, 2131492963, 2131492964, 2131492965, 2131492961 }, (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                switch (view.getId()) {
                    case 2131492962: {
                        MainActivity.this.clickStart();
                        break;
                    }
                    case 2131492964: {
                        MainActivity.this.startActivity(new Intent((Context)MainActivity.this, (Class)SatelliteActivity.class));
                        break;
                    }
                    case 2131492965: {
                        MainActivity.this.startActivity(new Intent((Context)MainActivity.this, (Class)XmlSettingsActivity.class));
                        break;
                    }
                    case 2131492963: {
                        MainActivity.this.clickMap();
                        break;
                    }
                    case 2131492961: {
                        MainActivity.this.getSupportFragmentManager().beginTransaction().add(new AboutDialog(), "DIALOG_TAG_MAIN").commitAllowingStateLoss();
                        break;
                    }
                }
            }
        }, null);
    }
    
    @Override
    protected void eventDestroyApp() {
        ((NotificationManager)this.getSystemService("notification")).cancelAll();
    }
    
    @Override
    protected void eventFirstInit() {
        VersionInfo.afterStartAction();
    }
    
    @Override
    protected void eventRegisterOnly() {
    }
    
    @Override
    protected void eventSecondInit() {
    }
    
    @Override
    protected String getCloseAdditionalText() {
        return null;
    }
    
    @Override
    protected int getCloseValue() {
        return 0;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (Build$VERSION.SDK_INT >= 23) {
            this.checkPermissions();
        }
        if ("android.intent.action.VIEW".equals(this.getIntent().getAction())) {
            final Intent intent = new Intent(this.getIntent());
            intent.setClass((Context)this, (Class)DownloadCartridgeActivity.class);
            this.startActivity(intent);
            this.finish();
        }
        else {
            if ("android.intent.action.SEND".equals(this.getIntent().getAction())) {
                while (true) {
                    Uri parse = null;
                    Label_0136: {
                        try {
                            parse = Uri.parse(this.getIntent().getStringExtra("android.intent.extra.TEXT"));
                            if (parse.getQueryParameter("CGUID") == null) {
                                throw new Exception("Invalid URL");
                            }
                            break Label_0136;
                        }
                        catch (Exception ex) {
                            ManagerNotify.toastShortMessage((Context)this, this.getString(2131165341));
                        }
                        this.finish();
                        return;
                    }
                    final Intent intent2 = new Intent((Context)this, (Class)DownloadCartridgeActivity.class);
                    intent2.setData(parse);
                    this.startActivity(intent2);
                    continue;
                }
            }
            String stringExtra;
            if (this.getIntent() == null) {
                stringExtra = null;
            }
            else {
                stringExtra = this.getIntent().getStringExtra("cguid");
            }
            if (stringExtra != null) {
                final File file = FileSystem.findFile(stringExtra);
                if (file != null) {
                    this.openCartridge(file);
                }
            }
        }
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(2131558400, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        boolean b = true;
        switch (menuItem.getItemId()) {
            case 2131492988: {
                this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://geocaching.com/")));
                return b;
            }
            case 2131492989: {
                this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://wherigo.com/")));
                return b;
            }
            case 2131492990: {
                this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/biylda/WhereYouGo")));
                break;
            }
        }
        b = false;
        return b;
    }
    
    @Override
    public void onRequestPermissionsResult(final int n, @NonNull final String[] array, @NonNull final int[] array2) {
        this.testFileSystem();
        if (Preferences.GPS || Preferences.GPS_START_AUTOMATICALLY) {
            LocationState.setGpsOn((Context)this);
        }
    }
    
    public void onResume() {
        super.onResume();
        refreshCartridges();
    }
}
