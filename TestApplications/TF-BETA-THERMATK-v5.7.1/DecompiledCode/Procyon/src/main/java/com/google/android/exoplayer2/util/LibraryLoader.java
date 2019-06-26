// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

public final class LibraryLoader
{
    private boolean isAvailable;
    private boolean loadAttempted;
    private String[] nativeLibraries;
    
    public LibraryLoader(final String... nativeLibraries) {
        this.nativeLibraries = nativeLibraries;
    }
    
    public boolean isAvailable() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        com/google/android/exoplayer2/util/LibraryLoader.loadAttempted:Z
        //     6: ifeq            18
        //     9: aload_0        
        //    10: getfield        com/google/android/exoplayer2/util/LibraryLoader.isAvailable:Z
        //    13: istore_1       
        //    14: aload_0        
        //    15: monitorexit    
        //    16: iload_1        
        //    17: ireturn        
        //    18: aload_0        
        //    19: iconst_1       
        //    20: putfield        com/google/android/exoplayer2/util/LibraryLoader.loadAttempted:Z
        //    23: aload_0        
        //    24: getfield        com/google/android/exoplayer2/util/LibraryLoader.nativeLibraries:[Ljava/lang/String;
        //    27: astore_2       
        //    28: aload_2        
        //    29: arraylength    
        //    30: istore_3       
        //    31: iconst_0       
        //    32: istore          4
        //    34: iload           4
        //    36: iload_3        
        //    37: if_icmpge       53
        //    40: aload_2        
        //    41: iload           4
        //    43: aaload         
        //    44: invokestatic    java/lang/System.loadLibrary:(Ljava/lang/String;)V
        //    47: iinc            4, 1
        //    50: goto            34
        //    53: aload_0        
        //    54: iconst_1       
        //    55: putfield        com/google/android/exoplayer2/util/LibraryLoader.isAvailable:Z
        //    58: aload_0        
        //    59: getfield        com/google/android/exoplayer2/util/LibraryLoader.isAvailable:Z
        //    62: istore_1       
        //    63: aload_0        
        //    64: monitorexit    
        //    65: iload_1        
        //    66: ireturn        
        //    67: astore_2       
        //    68: aload_0        
        //    69: monitorexit    
        //    70: aload_2        
        //    71: athrow         
        //    72: astore_2       
        //    73: goto            58
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  2      14     67     72     Any
        //  18     23     67     72     Any
        //  23     31     72     76     Ljava/lang/UnsatisfiedLinkError;
        //  23     31     67     72     Any
        //  40     47     72     76     Ljava/lang/UnsatisfiedLinkError;
        //  40     47     67     72     Any
        //  53     58     72     76     Ljava/lang/UnsatisfiedLinkError;
        //  53     58     67     72     Any
        //  58     63     67     72     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0034:
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
    
    public void setLibraries(final String... nativeLibraries) {
        synchronized (this) {
            Assertions.checkState(!this.loadAttempted, "Cannot set libraries after loading");
            this.nativeLibraries = nativeLibraries;
        }
    }
}
