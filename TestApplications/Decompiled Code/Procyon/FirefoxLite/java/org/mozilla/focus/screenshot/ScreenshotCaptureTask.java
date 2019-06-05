// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot;

import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.screenshot.model.Screenshot;
import org.mozilla.fileutils.FileUtils;
import android.text.TextUtils;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.io.IOException;
import android.graphics.Bitmap;
import android.content.Context;
import android.os.AsyncTask;

public class ScreenshotCaptureTask extends AsyncTask<Object, Void, String>
{
    private final Context context;
    
    public ScreenshotCaptureTask(final Context context) {
        this.context = context.getApplicationContext();
    }
    
    private static String saveBitmapToStorage(final Context p0, final String p1, final Bitmap p2) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    org/mozilla/focus/utils/StorageUtils.getTargetDirForSaveScreenshot:(Landroid/content/Context;)Ljava/io/File;
        //     4: astore_0       
        //     5: aload_0        
        //     6: invokestatic    org/mozilla/fileutils/FileUtils.ensureDir:(Ljava/io/File;)Z
        //     9: ifeq            90
        //    12: new             Ljava/io/File;
        //    15: dup            
        //    16: aload_0        
        //    17: aload_1        
        //    18: ldc             ".png"
        //    20: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //    23: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    26: astore_3       
        //    27: aconst_null    
        //    28: astore_0       
        //    29: new             Ljava/io/FileOutputStream;
        //    32: astore_1       
        //    33: aload_1        
        //    34: aload_3        
        //    35: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    38: aload_2        
        //    39: getstatic       android/graphics/Bitmap$CompressFormat.PNG:Landroid/graphics/Bitmap$CompressFormat;
        //    42: iconst_0       
        //    43: aload_1        
        //    44: invokevirtual   android/graphics/Bitmap.compress:(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
        //    47: ifeq            62
        //    50: aload_1        
        //    51: invokevirtual   java/io/FileOutputStream.flush:()V
        //    54: aload_3        
        //    55: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //    58: astore_0       
        //    59: goto            67
        //    62: aload_3        
        //    63: invokevirtual   java/io/File.delete:()Z
        //    66: pop            
        //    67: aload_1        
        //    68: invokevirtual   java/io/FileOutputStream.close:()V
        //    71: aload_0        
        //    72: areturn        
        //    73: astore_0       
        //    74: goto            80
        //    77: astore_0       
        //    78: aconst_null    
        //    79: astore_1       
        //    80: aload_1        
        //    81: ifnull          88
        //    84: aload_1        
        //    85: invokevirtual   java/io/FileOutputStream.close:()V
        //    88: aload_0        
        //    89: athrow         
        //    90: new             Ljava/io/IOException;
        //    93: dup            
        //    94: ldc             "Can't create folder"
        //    96: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //    99: athrow         
        //   100: astore_1       
        //   101: goto            71
        //   104: astore_1       
        //   105: goto            88
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  29     38     77     80     Any
        //  38     59     73     77     Any
        //  62     67     73     77     Any
        //  67     71     100    104    Ljava/io/IOException;
        //  84     88     104    108    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0067:
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
    
    protected String doInBackground(final Object... array) {
        final String s = (String)array[0];
        final String s2 = (String)array[1];
        final Bitmap bitmap = (Bitmap)array[2];
        final long currentTimeMillis = System.currentTimeMillis();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        try {
            final Context context = this.context;
            final StringBuilder sb = new StringBuilder();
            sb.append("Screenshot_");
            sb.append(simpleDateFormat.format(new Date(currentTimeMillis)));
            final String saveBitmapToStorage = saveBitmapToStorage(context, sb.toString(), bitmap);
            if (!TextUtils.isEmpty((CharSequence)saveBitmapToStorage)) {
                FileUtils.notifyMediaScanner(this.context, saveBitmapToStorage);
                ScreenshotManager.getInstance().insert(new Screenshot(s, s2, currentTimeMillis, saveBitmapToStorage), null);
                TelemetryWrapper.clickToolbarCapture(ScreenshotManager.getInstance().getCategory(this.context, s2), ScreenshotManager.getInstance().getCategoryVersion());
            }
            return saveBitmapToStorage;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
