// 
// Decompiled by Procyon v0.5.34
// 

package util;

import java.util.Vector;

public class BackgroundRunner extends Thread
{
    private static BackgroundRunner instance;
    private boolean end;
    private boolean paused;
    private Vector queue;
    private Runnable queueProcessedListener;
    
    public BackgroundRunner() {
        this.paused = false;
        this.queue = new Vector();
        this.end = false;
        this.queueProcessedListener = null;
        this.start();
    }
    
    public BackgroundRunner(final boolean paused) {
        this.paused = false;
        this.queue = new Vector();
        this.end = false;
        this.queueProcessedListener = null;
        this.paused = paused;
        this.start();
    }
    
    public static BackgroundRunner getInstance() {
        if (BackgroundRunner.instance == null) {
            BackgroundRunner.instance = new BackgroundRunner();
        }
        return BackgroundRunner.instance;
    }
    
    public static void performTask(final Runnable runnable) {
        getInstance().perform(runnable);
    }
    
    public void kill() {
        synchronized (this) {
            this.end = true;
            this.notify();
        }
    }
    
    public void pause() {
        synchronized (this) {
            this.paused = true;
        }
    }
    
    public void perform(final Runnable obj) {
        synchronized (this) {
            this.queue.addElement(obj);
            this.notify();
        }
    }
    
    @Override
    public void run() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        util/BackgroundRunner.end:Z
        //     4: ifne            31
        //     7: aload_0        
        //     8: monitorenter   
        //     9: aload_0        
        //    10: getfield        util/BackgroundRunner.paused:Z
        //    13: istore_1       
        //    14: iload_1        
        //    15: ifeq            32
        //    18: aload_0        
        //    19: invokevirtual   java/lang/Object.wait:()V
        //    22: aload_0        
        //    23: getfield        util/BackgroundRunner.end:Z
        //    26: ifeq            9
        //    29: aload_0        
        //    30: monitorexit    
        //    31: return         
        //    32: aload_0        
        //    33: monitorexit    
        //    34: iconst_0       
        //    35: istore_2       
        //    36: aload_0        
        //    37: getfield        util/BackgroundRunner.queue:Ljava/util/Vector;
        //    40: invokevirtual   java/util/Vector.isEmpty:()Z
        //    43: ifne            86
        //    46: iconst_1       
        //    47: istore_3       
        //    48: iconst_1       
        //    49: istore_2       
        //    50: aload_0        
        //    51: getfield        util/BackgroundRunner.queue:Ljava/util/Vector;
        //    54: invokevirtual   java/util/Vector.firstElement:()Ljava/lang/Object;
        //    57: checkcast       Ljava/lang/Runnable;
        //    60: astore          4
        //    62: aload_0        
        //    63: getfield        util/BackgroundRunner.queue:Ljava/util/Vector;
        //    66: iconst_0       
        //    67: invokevirtual   java/util/Vector.removeElementAt:(I)V
        //    70: aload           4
        //    72: invokeinterface java/lang/Runnable.run:()V
        //    77: aload_0        
        //    78: getfield        util/BackgroundRunner.paused:Z
        //    81: ifeq            36
        //    84: iload_3        
        //    85: istore_2       
        //    86: iload_2        
        //    87: ifeq            106
        //    90: aload_0        
        //    91: getfield        util/BackgroundRunner.queueProcessedListener:Ljava/lang/Runnable;
        //    94: ifnull          106
        //    97: aload_0        
        //    98: getfield        util/BackgroundRunner.queueProcessedListener:Ljava/lang/Runnable;
        //   101: invokeinterface java/lang/Runnable.run:()V
        //   106: aload_0        
        //   107: monitorenter   
        //   108: aload_0        
        //   109: getfield        util/BackgroundRunner.queue:Ljava/util/Vector;
        //   112: invokevirtual   java/util/Vector.isEmpty:()Z
        //   115: ifne            147
        //   118: aload_0        
        //   119: monitorexit    
        //   120: goto            0
        //   123: astore          4
        //   125: aload_0        
        //   126: monitorexit    
        //   127: aload           4
        //   129: athrow         
        //   130: astore          4
        //   132: aload_0        
        //   133: monitorexit    
        //   134: aload           4
        //   136: athrow         
        //   137: astore          4
        //   139: aload           4
        //   141: invokevirtual   java/lang/Throwable.printStackTrace:()V
        //   144: goto            77
        //   147: aload_0        
        //   148: getfield        util/BackgroundRunner.end:Z
        //   151: ifeq            159
        //   154: aload_0        
        //   155: monitorexit    
        //   156: goto            31
        //   159: aload_0        
        //   160: invokevirtual   java/lang/Object.wait:()V
        //   163: aload_0        
        //   164: monitorexit    
        //   165: goto            0
        //   168: astore          4
        //   170: goto            22
        //   173: astore          4
        //   175: goto            163
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  9      14     130    137    Any
        //  18     22     168    173    Ljava/lang/InterruptedException;
        //  18     22     130    137    Any
        //  22     31     130    137    Any
        //  32     34     130    137    Any
        //  70     77     137    147    Ljava/lang/Throwable;
        //  108    120    123    130    Any
        //  125    127    123    130    Any
        //  132    134    130    137    Any
        //  147    156    123    130    Any
        //  159    163    173    178    Ljava/lang/InterruptedException;
        //  159    163    123    130    Any
        //  163    165    123    130    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0022:
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
    
    public void setQueueListener(final Runnable queueProcessedListener) {
        this.queueProcessedListener = queueProcessedListener;
    }
    
    public void unpause() {
        synchronized (this) {
            this.paused = false;
            this.notify();
        }
    }
}
