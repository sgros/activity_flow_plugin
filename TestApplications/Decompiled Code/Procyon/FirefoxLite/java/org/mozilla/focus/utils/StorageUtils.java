// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import org.mozilla.fileutils.FileUtils;
import android.os.Environment;
import java.io.File;
import android.content.Context;

public class StorageUtils
{
    public static File getAppMediaDirOnRemovableStorage(final Context context) throws NoRemovableStorageException {
        final File firstRemovableMedia = getFirstRemovableMedia(context);
        if (firstRemovableMedia == null) {
            throw new NoRemovableStorageException("No removable media to use");
        }
        if ("mounted".equals(Environment.getExternalStorageState(firstRemovableMedia))) {
            return firstRemovableMedia;
        }
        throw new NoRemovableStorageException("No mounted-removable media to use");
    }
    
    static File getFirstRemovableMedia(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   android/content/Context.getExternalMediaDirs:()[Ljava/io/File;
        //     4: astore_0       
        //     5: aload_0        
        //     6: arraylength    
        //     7: istore_1       
        //     8: iconst_0       
        //     9: istore_2       
        //    10: iload_2        
        //    11: iload_1        
        //    12: if_icmpge       47
        //    15: aload_0        
        //    16: iload_2        
        //    17: aaload         
        //    18: astore_3       
        //    19: aload_3        
        //    20: ifnull          41
        //    23: aload_3        
        //    24: invokestatic    android/os/Environment.isExternalStorageRemovable:(Ljava/io/File;)Z
        //    27: istore          4
        //    29: iload           4
        //    31: ifeq            41
        //    34: aload_3        
        //    35: areturn        
        //    36: astore_3       
        //    37: aload_3        
        //    38: invokevirtual   java/lang/IllegalArgumentException.printStackTrace:()V
        //    41: iinc            2, 1
        //    44: goto            10
        //    47: aconst_null    
        //    48: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  23     29     36     41     Ljava/lang/IllegalArgumentException;
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
    
    public static File getTargetDirForSaveScreenshot(final Context context) {
        final String replaceAll = context.getString(2131755062).replaceAll(" ", "");
        final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), replaceAll);
        FileUtils.ensureDir(file);
        if (!Settings.getInstance(context).shouldSaveToRemovableStorage()) {
            return file;
        }
        try {
            return new File(getAppMediaDirOnRemovableStorage(context), replaceAll);
        }
        catch (NoRemovableStorageException ex) {
            return file;
        }
    }
    
    public static File getTargetDirOnRemovableStorageForDownloads(final Context context, final String s) throws NoRemovableStorageException {
        if (!Settings.getInstance(context).shouldSaveToRemovableStorage()) {
            return null;
        }
        final File file = new File(getAppMediaDirOnRemovableStorage(context), "downloads");
        if (MimeUtils.isImage(s)) {
            return new File(file, "pictures");
        }
        return new File(file, "others");
    }
}
