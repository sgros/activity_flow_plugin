// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android;

import java.util.ArrayList;
import locus.api.objects.Storable;
import android.content.Intent;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import android.content.Context;
import locus.api.android.objects.PackWaypoints;
import java.util.List;

public class ActionDisplayPoints extends ActionDisplay
{
    private static final String TAG = "ActionDisplayPoints";
    
    public static List<PackWaypoints> readDataWriteOnCard(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0        
        //     5: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //     8: astore_1       
        //     9: aload_1        
        //    10: invokevirtual   java/io/File.exists:()Z
        //    13: ifne            26
        //    16: new             Ljava/util/ArrayList;
        //    19: dup            
        //    20: invokespecial   java/util/ArrayList.<init>:()V
        //    23: astore_0       
        //    24: aload_0        
        //    25: areturn        
        //    26: aconst_null    
        //    27: astore_2       
        //    28: aconst_null    
        //    29: astore_3       
        //    30: aload_2        
        //    31: astore          4
        //    33: new             Ljava/io/DataInputStream;
        //    36: astore          5
        //    38: aload_2        
        //    39: astore          4
        //    41: new             Ljava/io/FileInputStream;
        //    44: astore          6
        //    46: aload_2        
        //    47: astore          4
        //    49: aload           6
        //    51: aload_1        
        //    52: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    55: aload_2        
        //    56: astore          4
        //    58: aload           5
        //    60: aload           6
        //    62: invokespecial   java/io/DataInputStream.<init>:(Ljava/io/InputStream;)V
        //    65: ldc             Llocus/api/android/objects/PackWaypoints;.class
        //    67: aload           5
        //    69: invokestatic    locus/api/objects/Storable.readList:(Ljava/lang/Class;Ljava/io/DataInputStream;)Ljava/util/List;
        //    72: astore          4
        //    74: aload           4
        //    76: astore_0       
        //    77: aload           5
        //    79: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //    82: goto            24
        //    85: astore_2       
        //    86: aload_3        
        //    87: astore          5
        //    89: aload           5
        //    91: astore          4
        //    93: new             Ljava/lang/StringBuilder;
        //    96: astore_3       
        //    97: aload           5
        //    99: astore          4
        //   101: aload_3        
        //   102: invokespecial   java/lang/StringBuilder.<init>:()V
        //   105: aload           5
        //   107: astore          4
        //   109: ldc             "ActionDisplayPoints"
        //   111: aload_3        
        //   112: ldc             "getDataFile("
        //   114: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   117: aload_0        
        //   118: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   121: ldc             ")"
        //   123: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   126: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   129: aload_2        
        //   130: invokestatic    locus/api/utils/Logger.logE:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Exception;)V
        //   133: aload           5
        //   135: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   138: new             Ljava/util/ArrayList;
        //   141: dup            
        //   142: invokespecial   java/util/ArrayList.<init>:()V
        //   145: astore_0       
        //   146: goto            24
        //   149: astore_0       
        //   150: aload           4
        //   152: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   155: aload_0        
        //   156: athrow         
        //   157: astore_0       
        //   158: aload           5
        //   160: astore          4
        //   162: goto            150
        //   165: astore_2       
        //   166: goto            89
        //    Signature:
        //  (Ljava/lang/String;)Ljava/util/List<Llocus/api/android/objects/PackWaypoints;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  33     38     85     89     Ljava/lang/Exception;
        //  33     38     149    150    Any
        //  41     46     85     89     Ljava/lang/Exception;
        //  41     46     149    150    Any
        //  49     55     85     89     Ljava/lang/Exception;
        //  49     55     149    150    Any
        //  58     65     85     89     Ljava/lang/Exception;
        //  58     65     149    150    Any
        //  65     74     165    169    Ljava/lang/Exception;
        //  65     74     157    165    Any
        //  93     97     149    150    Any
        //  101    105    149    150    Any
        //  109    133    149    150    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0089:
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
    
    private static boolean sendDataWriteOnCard(final List<PackWaypoints> p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_2       
        //     2: iload_2        
        //     3: istore_3       
        //     4: aload_0        
        //     5: ifnull          19
        //     8: aload_0        
        //     9: invokeinterface java/util/List.size:()I
        //    14: ifne            21
        //    17: iload_2        
        //    18: istore_3       
        //    19: iload_3        
        //    20: ireturn        
        //    21: aconst_null    
        //    22: astore          4
        //    24: aconst_null    
        //    25: astore          5
        //    27: aload           4
        //    29: astore          6
        //    31: new             Ljava/io/File;
        //    34: astore          7
        //    36: aload           4
        //    38: astore          6
        //    40: aload           7
        //    42: aload_1        
        //    43: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    46: aload           4
        //    48: astore          6
        //    50: aload           7
        //    52: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //    55: invokevirtual   java/io/File.mkdirs:()Z
        //    58: pop            
        //    59: aload           4
        //    61: astore          6
        //    63: aload           7
        //    65: invokevirtual   java/io/File.exists:()Z
        //    68: ifeq            81
        //    71: aload           4
        //    73: astore          6
        //    75: aload           7
        //    77: invokevirtual   java/io/File.delete:()Z
        //    80: pop            
        //    81: aload           4
        //    83: astore          6
        //    85: new             Ljava/io/DataOutputStream;
        //    88: astore          8
        //    90: aload           4
        //    92: astore          6
        //    94: new             Ljava/io/FileOutputStream;
        //    97: astore          9
        //    99: aload           4
        //   101: astore          6
        //   103: aload           9
        //   105: aload           7
        //   107: iconst_0       
        //   108: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;Z)V
        //   111: aload           4
        //   113: astore          6
        //   115: aload           8
        //   117: aload           9
        //   119: invokespecial   java/io/DataOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   122: aload_0        
        //   123: aload           8
        //   125: invokestatic    locus/api/objects/Storable.writeList:(Ljava/util/List;Ljava/io/DataOutputStream;)V
        //   128: aload           8
        //   130: invokevirtual   java/io/DataOutputStream.flush:()V
        //   133: iconst_1       
        //   134: istore_3       
        //   135: aload           8
        //   137: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   140: goto            19
        //   143: astore          4
        //   145: aload           5
        //   147: astore          8
        //   149: aload           8
        //   151: astore          6
        //   153: new             Ljava/lang/StringBuilder;
        //   156: astore          5
        //   158: aload           8
        //   160: astore          6
        //   162: aload           5
        //   164: invokespecial   java/lang/StringBuilder.<init>:()V
        //   167: aload           8
        //   169: astore          6
        //   171: ldc             "ActionDisplayPoints"
        //   173: aload           5
        //   175: ldc             "sendDataWriteOnCard("
        //   177: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   180: aload_1        
        //   181: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   184: ldc             ", "
        //   186: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   189: aload_0        
        //   190: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   193: ldc             ")"
        //   195: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   198: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   201: aload           4
        //   203: invokestatic    locus/api/utils/Logger.logE:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Exception;)V
        //   206: aload           8
        //   208: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   211: iload_2        
        //   212: istore_3       
        //   213: goto            19
        //   216: astore_0       
        //   217: aload           6
        //   219: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   222: aload_0        
        //   223: athrow         
        //   224: astore_0       
        //   225: aload           8
        //   227: astore          6
        //   229: goto            217
        //   232: astore          4
        //   234: goto            149
        //    Signature:
        //  (Ljava/util/List<Llocus/api/android/objects/PackWaypoints;>;Ljava/lang/String;)Z
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  31     36     143    149    Ljava/lang/Exception;
        //  31     36     216    217    Any
        //  40     46     143    149    Ljava/lang/Exception;
        //  40     46     216    217    Any
        //  50     59     143    149    Ljava/lang/Exception;
        //  50     59     216    217    Any
        //  63     71     143    149    Ljava/lang/Exception;
        //  63     71     216    217    Any
        //  75     81     143    149    Ljava/lang/Exception;
        //  75     81     216    217    Any
        //  85     90     143    149    Ljava/lang/Exception;
        //  85     90     216    217    Any
        //  94     99     143    149    Ljava/lang/Exception;
        //  94     99     216    217    Any
        //  103    111    143    149    Ljava/lang/Exception;
        //  103    111    216    217    Any
        //  115    122    143    149    Ljava/lang/Exception;
        //  115    122    216    217    Any
        //  122    133    232    237    Ljava/lang/Exception;
        //  122    133    224    232    Any
        //  153    158    216    217    Any
        //  162    167    216    217    Any
        //  171    206    216    217    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0149:
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
    
    public static boolean sendPack(final Context context, final PackWaypoints packWaypoints, final ExtraAction extraAction) throws RequiredVersionMissingException {
        boolean b = true;
        final boolean b2 = extraAction == ExtraAction.IMPORT;
        if (extraAction != ExtraAction.CENTER) {
            b = false;
        }
        return sendPack("locus.api.android.ACTION_DISPLAY_DATA", context, packWaypoints, b2, b);
    }
    
    private static boolean sendPack(final String s, final Context context, final PackWaypoints packWaypoints, final boolean b, final boolean b2) throws RequiredVersionMissingException {
        boolean sendData;
        if (packWaypoints == null) {
            sendData = false;
        }
        else {
            final Intent intent = new Intent();
            intent.putExtra("INTENT_EXTRA_POINTS_DATA", packWaypoints.getAsBytes());
            sendData = ActionDisplay.sendData(s, context, intent, b, b2);
        }
        return sendData;
    }
    
    public static boolean sendPackSilent(final Context context, final PackWaypoints packWaypoints, final boolean b) throws RequiredVersionMissingException {
        return sendPack("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", context, packWaypoints, false, b);
    }
    
    public static boolean sendPacks(final Context context, final List<PackWaypoints> list, final ExtraAction extraAction) throws RequiredVersionMissingException {
        boolean b = true;
        final boolean b2 = extraAction == ExtraAction.IMPORT;
        if (extraAction != ExtraAction.CENTER) {
            b = false;
        }
        return sendPacks("locus.api.android.ACTION_DISPLAY_DATA", context, list, b2, b);
    }
    
    private static boolean sendPacks(final String s, final Context context, final List<PackWaypoints> list, final boolean b, final boolean b2) throws RequiredVersionMissingException {
        boolean sendData;
        if (list == null) {
            sendData = false;
        }
        else {
            final Intent intent = new Intent();
            intent.putExtra("INTENT_EXTRA_POINTS_DATA_ARRAY", Storable.getAsBytes(list));
            sendData = ActionDisplay.sendData(s, context, intent, b, b2);
        }
        return sendData;
    }
    
    public static boolean sendPacksFile(final Context context, final ArrayList<PackWaypoints> list, final String s, final ExtraAction extraAction) throws RequiredVersionMissingException {
        return sendPacksFile("locus.api.android.ACTION_DISPLAY_DATA", context, list, s, extraAction == ExtraAction.IMPORT, extraAction == ExtraAction.CENTER);
    }
    
    private static boolean sendPacksFile(final String s, final Context context, final List<PackWaypoints> list, final String s2, final boolean b, final boolean b2) throws RequiredVersionMissingException {
        boolean sendData;
        if (sendDataWriteOnCard(list, s2)) {
            final Intent intent = new Intent();
            intent.putExtra("INTENT_EXTRA_POINTS_FILE_PATH", s2);
            sendData = ActionDisplay.sendData(s, context, intent, b, b2);
        }
        else {
            sendData = false;
        }
        return sendData;
    }
    
    public static boolean sendPacksFileSilent(final Context context, final ArrayList<PackWaypoints> list, final String s, final boolean b) throws RequiredVersionMissingException {
        return sendPacksFile("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", context, list, s, false, b);
    }
    
    public static boolean sendPacksSilent(final Context context, final List<PackWaypoints> list, final boolean b) throws RequiredVersionMissingException {
        return sendPacks("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", context, list, false, b);
    }
    
    public void removePackFromLocus(final Context context, final String s) throws RequiredVersionMissingException {
        if (s != null && s.length() != 0) {
            final PackWaypoints packWaypoints = new PackWaypoints(s);
            new Intent().putExtra("INTENT_EXTRA_POINTS_DATA", packWaypoints.getAsBytes());
            sendPackSilent(context, packWaypoints, false);
        }
    }
}
