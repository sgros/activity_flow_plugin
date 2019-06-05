// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.provider;

import java.util.concurrent.Callable;
import android.support.annotation.VisibleForTesting;
import android.os.Message;
import android.os.HandlerThread;
import android.os.Handler;
import android.support.annotation.GuardedBy;
import android.os.Handler$Callback;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class SelfDestructiveThread
{
    private static final int MSG_DESTRUCTION = 0;
    private static final int MSG_INVOKE_RUNNABLE = 1;
    private Handler$Callback mCallback;
    private final int mDestructAfterMillisec;
    @GuardedBy("mLock")
    private int mGeneration;
    @GuardedBy("mLock")
    private Handler mHandler;
    private final Object mLock;
    private final int mPriority;
    @GuardedBy("mLock")
    private HandlerThread mThread;
    private final String mThreadName;
    
    public SelfDestructiveThread(final String mThreadName, final int mPriority, final int mDestructAfterMillisec) {
        this.mLock = new Object();
        this.mCallback = (Handler$Callback)new Handler$Callback() {
            public boolean handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        return true;
                    }
                    case 1: {
                        SelfDestructiveThread.this.onInvokeRunnable((Runnable)message.obj);
                        return true;
                    }
                    case 0: {
                        SelfDestructiveThread.this.onDestruction();
                        return true;
                    }
                }
            }
        };
        this.mThreadName = mThreadName;
        this.mPriority = mPriority;
        this.mDestructAfterMillisec = mDestructAfterMillisec;
        this.mGeneration = 0;
    }
    
    private void onDestruction() {
        synchronized (this.mLock) {
            if (this.mHandler.hasMessages(1)) {
                return;
            }
            this.mThread.quit();
            this.mThread = null;
            this.mHandler = null;
        }
    }
    
    private void onInvokeRunnable(final Runnable runnable) {
        runnable.run();
        synchronized (this.mLock) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), (long)this.mDestructAfterMillisec);
        }
    }
    
    private void post(final Runnable runnable) {
        synchronized (this.mLock) {
            if (this.mThread == null) {
                (this.mThread = new HandlerThread(this.mThreadName, this.mPriority)).start();
                this.mHandler = new Handler(this.mThread.getLooper(), this.mCallback);
                ++this.mGeneration;
            }
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, (Object)runnable));
        }
    }
    
    @VisibleForTesting
    public int getGeneration() {
        synchronized (this.mLock) {
            return this.mGeneration;
        }
    }
    
    @VisibleForTesting
    public boolean isRunning() {
        synchronized (this.mLock) {
            return this.mThread != null;
        }
    }
    
    public <T> void postAndReply(final Callable<T> callable, final ReplyCallback<T> replyCallback) {
        this.post(new Runnable() {
            final /* synthetic */ Handler val$callingHandler = new Handler();
            
            @Override
            public void run() {
                Object call;
                try {
                    call = callable.call();
                }
                catch (Exception ex) {
                    call = null;
                }
                this.val$callingHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        replyCallback.onReply(call);
                    }
                });
            }
        });
    }
    
    public <T> T postAndWait(final Callable<T> p0, final int p1) throws InterruptedException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/concurrent/locks/ReentrantLock.<init>:()V
        //     7: astore_3       
        //     8: aload_3        
        //     9: invokevirtual   java/util/concurrent/locks/ReentrantLock.newCondition:()Ljava/util/concurrent/locks/Condition;
        //    12: astore          4
        //    14: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    17: dup            
        //    18: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    21: astore          5
        //    23: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    26: dup            
        //    27: iconst_1       
        //    28: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    31: astore          6
        //    33: aload_0        
        //    34: new             Landroid/support/v4/provider/SelfDestructiveThread$3;
        //    37: dup            
        //    38: aload_0        
        //    39: aload           5
        //    41: aload_1        
        //    42: aload_3        
        //    43: aload           6
        //    45: aload           4
        //    47: invokespecial   android/support/v4/provider/SelfDestructiveThread$3.<init>:(Landroid/support/v4/provider/SelfDestructiveThread;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/Callable;Ljava/util/concurrent/locks/ReentrantLock;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/locks/Condition;)V
        //    50: invokespecial   android/support/v4/provider/SelfDestructiveThread.post:(Ljava/lang/Runnable;)V
        //    53: aload_3        
        //    54: invokevirtual   java/util/concurrent/locks/ReentrantLock.lock:()V
        //    57: aload           6
        //    59: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //    62: ifne            77
        //    65: aload           5
        //    67: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //    70: astore_1       
        //    71: aload_3        
        //    72: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //    75: aload_1        
        //    76: areturn        
        //    77: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //    80: iload_2        
        //    81: i2l            
        //    82: invokevirtual   java/util/concurrent/TimeUnit.toNanos:(J)J
        //    85: lstore          7
        //    87: aload           4
        //    89: lload           7
        //    91: invokeinterface java/util/concurrent/locks/Condition.awaitNanos:(J)J
        //    96: lstore          9
        //    98: aload           6
        //   100: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   103: ifne            118
        //   106: aload           5
        //   108: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   111: astore_1       
        //   112: aload_3        
        //   113: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   116: aload_1        
        //   117: areturn        
        //   118: lload           9
        //   120: lstore          7
        //   122: lload           9
        //   124: lconst_0       
        //   125: lcmp           
        //   126: ifgt            87
        //   129: new             Ljava/lang/InterruptedException;
        //   132: astore_1       
        //   133: aload_1        
        //   134: ldc             "timeout"
        //   136: invokespecial   java/lang/InterruptedException.<init>:(Ljava/lang/String;)V
        //   139: aload_1        
        //   140: athrow         
        //   141: astore_1       
        //   142: aload_3        
        //   143: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   146: aload_1        
        //   147: athrow         
        //   148: astore_1       
        //   149: lload           7
        //   151: lstore          9
        //   153: goto            98
        //    Exceptions:
        //  throws java.lang.InterruptedException
        //    Signature:
        //  <T:Ljava/lang/Object;>(Ljava/util/concurrent/Callable<TT;>;I)TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  57     71     141    148    Any
        //  77     87     141    148    Any
        //  87     98     148    156    Ljava/lang/InterruptedException;
        //  87     98     141    148    Any
        //  98     112    141    148    Any
        //  129    141    141    148    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0087:
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
    
    public interface ReplyCallback<T>
    {
        void onReply(final T p0);
    }
}
