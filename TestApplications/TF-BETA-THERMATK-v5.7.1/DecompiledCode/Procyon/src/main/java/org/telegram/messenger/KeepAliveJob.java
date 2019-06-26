// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Context;
import android.content.Intent;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.support.JobIntentService;

public class KeepAliveJob extends JobIntentService
{
    private static volatile CountDownLatch countDownLatch;
    private static Runnable finishJobByTimeoutRunnable;
    private static volatile boolean startingJob;
    private static final Object sync;
    
    static {
        sync = new Object();
        KeepAliveJob.finishJobByTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                finishJobInternal();
            }
        };
    }
    
    public static void finishJob() {
        Utilities.globalQueue.postRunnable(new Runnable() {
            @Override
            public void run() {
                finishJobInternal();
            }
        });
    }
    
    private static void finishJobInternal() {
        synchronized (KeepAliveJob.sync) {
            if (KeepAliveJob.countDownLatch != null) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("finish keep-alive job");
                }
                KeepAliveJob.countDownLatch.countDown();
            }
            if (KeepAliveJob.startingJob) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("finish queued keep-alive job");
                }
                KeepAliveJob.startingJob = false;
            }
        }
    }
    
    public static void startJob() {
        Utilities.globalQueue.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (KeepAliveJob.startingJob) {
                    return;
                }
                if (KeepAliveJob.countDownLatch != null) {
                    return;
                }
                try {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("starting keep-alive job");
                    }
                    Object access$200 = KeepAliveJob.sync;
                    synchronized (access$200) {
                        KeepAliveJob.startingJob = true;
                        // monitorexit(access$200)
                        final Context applicationContext = ApplicationLoader.applicationContext;
                        access$200 = new Intent();
                        JobIntentService.enqueueWork(applicationContext, KeepAliveJob.class, 1000, (Intent)access$200);
                    }
                }
                catch (Exception ex) {}
            }
        });
    }
    
    @Override
    protected void onHandleWork(final Intent p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_1       
        //     4: aload_1        
        //     5: monitorenter   
        //     6: getstatic       org/telegram/messenger/KeepAliveJob.startingJob:Z
        //     9: ifne            15
        //    12: aload_1        
        //    13: monitorexit    
        //    14: return         
        //    15: new             Ljava/util/concurrent/CountDownLatch;
        //    18: astore_2       
        //    19: aload_2        
        //    20: iconst_1       
        //    21: invokespecial   java/util/concurrent/CountDownLatch.<init>:(I)V
        //    24: aload_2        
        //    25: putstatic       org/telegram/messenger/KeepAliveJob.countDownLatch:Ljava/util/concurrent/CountDownLatch;
        //    28: aload_1        
        //    29: monitorexit    
        //    30: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //    33: ifeq            41
        //    36: ldc             "started keep-alive job"
        //    38: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //    41: getstatic       org/telegram/messenger/Utilities.globalQueue:Lorg/telegram/messenger/DispatchQueue;
        //    44: getstatic       org/telegram/messenger/KeepAliveJob.finishJobByTimeoutRunnable:Ljava/lang/Runnable;
        //    47: ldc2_w          60000
        //    50: invokevirtual   org/telegram/messenger/DispatchQueue.postRunnable:(Ljava/lang/Runnable;J)V
        //    53: getstatic       org/telegram/messenger/KeepAliveJob.countDownLatch:Ljava/util/concurrent/CountDownLatch;
        //    56: invokevirtual   java/util/concurrent/CountDownLatch.await:()V
        //    59: getstatic       org/telegram/messenger/Utilities.globalQueue:Lorg/telegram/messenger/DispatchQueue;
        //    62: getstatic       org/telegram/messenger/KeepAliveJob.finishJobByTimeoutRunnable:Ljava/lang/Runnable;
        //    65: invokevirtual   org/telegram/messenger/DispatchQueue.cancelRunnable:(Ljava/lang/Runnable;)V
        //    68: getstatic       org/telegram/messenger/KeepAliveJob.sync:Ljava/lang/Object;
        //    71: astore_1       
        //    72: aload_1        
        //    73: monitorenter   
        //    74: aconst_null    
        //    75: putstatic       org/telegram/messenger/KeepAliveJob.countDownLatch:Ljava/util/concurrent/CountDownLatch;
        //    78: aload_1        
        //    79: monitorexit    
        //    80: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //    83: ifeq            91
        //    86: ldc             "ended keep-alive job"
        //    88: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //    91: return         
        //    92: astore_2       
        //    93: aload_1        
        //    94: monitorexit    
        //    95: aload_2        
        //    96: athrow         
        //    97: astore_2       
        //    98: aload_1        
        //    99: monitorexit    
        //   100: aload_2        
        //   101: athrow         
        //   102: astore_1       
        //   103: goto            59
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  6      14     97     102    Any
        //  15     30     97     102    Any
        //  53     59     102    106    Ljava/lang/Throwable;
        //  74     80     92     97     Any
        //  93     95     92     97     Any
        //  98     100    97     102    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0059:
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
}
