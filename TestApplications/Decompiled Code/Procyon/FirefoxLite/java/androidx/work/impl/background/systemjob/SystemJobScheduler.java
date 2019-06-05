// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemjob;

import androidx.work.impl.model.WorkSpec;
import android.os.Build$VERSION;
import android.os.PersistableBundle;
import java.util.Iterator;
import java.util.List;
import android.app.job.JobInfo;
import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;
import android.app.job.JobScheduler;
import androidx.work.impl.utils.IdGenerator;
import androidx.work.impl.Scheduler;

public class SystemJobScheduler implements Scheduler
{
    private static final String TAG;
    private final IdGenerator mIdGenerator;
    private final JobScheduler mJobScheduler;
    private final SystemJobInfoConverter mSystemJobInfoConverter;
    private final WorkManagerImpl mWorkManager;
    
    static {
        TAG = Logger.tagWithPrefix("SystemJobScheduler");
    }
    
    public SystemJobScheduler(final Context context, final WorkManagerImpl workManagerImpl) {
        this(context, workManagerImpl, (JobScheduler)context.getSystemService("jobscheduler"), new SystemJobInfoConverter(context));
    }
    
    public SystemJobScheduler(final Context context, final WorkManagerImpl mWorkManager, final JobScheduler mJobScheduler, final SystemJobInfoConverter mSystemJobInfoConverter) {
        this.mWorkManager = mWorkManager;
        this.mJobScheduler = mJobScheduler;
        this.mIdGenerator = new IdGenerator(context);
        this.mSystemJobInfoConverter = mSystemJobInfoConverter;
    }
    
    private static JobInfo getPendingJobInfo(final JobScheduler jobScheduler, final String s) {
        final List allPendingJobs = jobScheduler.getAllPendingJobs();
        if (allPendingJobs != null) {
            for (final JobInfo jobInfo : allPendingJobs) {
                final PersistableBundle extras = jobInfo.getExtras();
                if (extras != null && extras.containsKey("EXTRA_WORK_SPEC_ID") && s.equals(extras.getString("EXTRA_WORK_SPEC_ID"))) {
                    return jobInfo;
                }
            }
        }
        return null;
    }
    
    public static void jobSchedulerCancelAll(final Context context) {
        final JobScheduler jobScheduler = (JobScheduler)context.getSystemService("jobscheduler");
        if (jobScheduler != null) {
            final List allPendingJobs = jobScheduler.getAllPendingJobs();
            if (allPendingJobs != null) {
                for (final JobInfo jobInfo : allPendingJobs) {
                    if (jobInfo.getExtras().containsKey("EXTRA_WORK_SPEC_ID")) {
                        jobScheduler.cancel(jobInfo.getId());
                    }
                }
            }
        }
    }
    
    @Override
    public void cancel(final String s) {
        final List allPendingJobs = this.mJobScheduler.getAllPendingJobs();
        if (allPendingJobs != null) {
            for (final JobInfo jobInfo : allPendingJobs) {
                if (s.equals(jobInfo.getExtras().getString("EXTRA_WORK_SPEC_ID"))) {
                    this.mWorkManager.getWorkDatabase().systemIdInfoDao().removeSystemIdInfo(s);
                    this.mJobScheduler.cancel(jobInfo.getId());
                    if (Build$VERSION.SDK_INT != 23) {
                        return;
                    }
                    continue;
                }
            }
        }
    }
    
    @Override
    public void schedule(final WorkSpec... p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        androidx/work/impl/background/systemjob/SystemJobScheduler.mWorkManager:Landroidx/work/impl/WorkManagerImpl;
        //     4: invokevirtual   androidx/work/impl/WorkManagerImpl.getWorkDatabase:()Landroidx/work/impl/WorkDatabase;
        //     7: astore_2       
        //     8: aload_1        
        //     9: arraylength    
        //    10: istore_3       
        //    11: iconst_0       
        //    12: istore          4
        //    14: iload           4
        //    16: iload_3        
        //    17: if_icmpge       419
        //    20: aload_1        
        //    21: iload           4
        //    23: aaload         
        //    24: astore          5
        //    26: aload_2        
        //    27: invokevirtual   androidx/work/impl/WorkDatabase.beginTransaction:()V
        //    30: aload_2        
        //    31: invokevirtual   androidx/work/impl/WorkDatabase.workSpecDao:()Landroidx/work/impl/model/WorkSpecDao;
        //    34: aload           5
        //    36: getfield        androidx/work/impl/model/WorkSpec.id:Ljava/lang/String;
        //    39: invokeinterface androidx/work/impl/model/WorkSpecDao.getWorkSpec:(Ljava/lang/String;)Landroidx/work/impl/model/WorkSpec;
        //    44: astore          6
        //    46: aload           6
        //    48: ifnonnull       121
        //    51: invokestatic    androidx/work/Logger.get:()Landroidx/work/Logger;
        //    54: astore          7
        //    56: getstatic       androidx/work/impl/background/systemjob/SystemJobScheduler.TAG:Ljava/lang/String;
        //    59: astore          6
        //    61: new             Ljava/lang/StringBuilder;
        //    64: astore          8
        //    66: aload           8
        //    68: invokespecial   java/lang/StringBuilder.<init>:()V
        //    71: aload           8
        //    73: ldc             "Skipping scheduling "
        //    75: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    78: pop            
        //    79: aload           8
        //    81: aload           5
        //    83: getfield        androidx/work/impl/model/WorkSpec.id:Ljava/lang/String;
        //    86: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    89: pop            
        //    90: aload           8
        //    92: ldc             " because it's no longer in the DB"
        //    94: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    97: pop            
        //    98: aload           7
        //   100: aload           6
        //   102: aload           8
        //   104: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   107: iconst_0       
        //   108: anewarray       Ljava/lang/Throwable;
        //   111: invokevirtual   androidx/work/Logger.warning:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Throwable;)V
        //   114: aload_2        
        //   115: invokevirtual   androidx/work/impl/WorkDatabase.endTransaction:()V
        //   118: goto            406
        //   121: aload           6
        //   123: getfield        androidx/work/impl/model/WorkSpec.state:Landroidx/work/WorkInfo$State;
        //   126: getstatic       androidx/work/WorkInfo$State.ENQUEUED:Landroidx/work/WorkInfo$State;
        //   129: if_acmpeq       198
        //   132: invokestatic    androidx/work/Logger.get:()Landroidx/work/Logger;
        //   135: astore          7
        //   137: getstatic       androidx/work/impl/background/systemjob/SystemJobScheduler.TAG:Ljava/lang/String;
        //   140: astore          8
        //   142: new             Ljava/lang/StringBuilder;
        //   145: astore          6
        //   147: aload           6
        //   149: invokespecial   java/lang/StringBuilder.<init>:()V
        //   152: aload           6
        //   154: ldc             "Skipping scheduling "
        //   156: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   159: pop            
        //   160: aload           6
        //   162: aload           5
        //   164: getfield        androidx/work/impl/model/WorkSpec.id:Ljava/lang/String;
        //   167: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   170: pop            
        //   171: aload           6
        //   173: ldc             " because it is no longer enqueued"
        //   175: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   178: pop            
        //   179: aload           7
        //   181: aload           8
        //   183: aload           6
        //   185: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   188: iconst_0       
        //   189: anewarray       Ljava/lang/Throwable;
        //   192: invokevirtual   androidx/work/Logger.warning:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Throwable;)V
        //   195: goto            114
        //   198: aload_2        
        //   199: invokevirtual   androidx/work/impl/WorkDatabase.systemIdInfoDao:()Landroidx/work/impl/model/SystemIdInfoDao;
        //   202: aload           5
        //   204: getfield        androidx/work/impl/model/WorkSpec.id:Ljava/lang/String;
        //   207: invokeinterface androidx/work/impl/model/SystemIdInfoDao.getSystemIdInfo:(Ljava/lang/String;)Landroidx/work/impl/model/SystemIdInfo;
        //   212: astore          6
        //   214: aload           6
        //   216: ifnull          267
        //   219: aload_0        
        //   220: getfield        androidx/work/impl/background/systemjob/SystemJobScheduler.mJobScheduler:Landroid/app/job/JobScheduler;
        //   223: aload           5
        //   225: getfield        androidx/work/impl/model/WorkSpec.id:Ljava/lang/String;
        //   228: invokestatic    androidx/work/impl/background/systemjob/SystemJobScheduler.getPendingJobInfo:(Landroid/app/job/JobScheduler;Ljava/lang/String;)Landroid/app/job/JobInfo;
        //   231: ifnull          267
        //   234: invokestatic    androidx/work/Logger.get:()Landroidx/work/Logger;
        //   237: getstatic       androidx/work/impl/background/systemjob/SystemJobScheduler.TAG:Ljava/lang/String;
        //   240: ldc             "Skipping scheduling %s because JobScheduler is aware of it already."
        //   242: iconst_1       
        //   243: anewarray       Ljava/lang/Object;
        //   246: dup            
        //   247: iconst_0       
        //   248: aload           5
        //   250: getfield        androidx/work/impl/model/WorkSpec.id:Ljava/lang/String;
        //   253: aastore        
        //   254: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   257: iconst_0       
        //   258: anewarray       Ljava/lang/Throwable;
        //   261: invokevirtual   androidx/work/Logger.debug:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Throwable;)V
        //   264: goto            114
        //   267: aload           6
        //   269: ifnull          282
        //   272: aload           6
        //   274: getfield        androidx/work/impl/model/SystemIdInfo.systemId:I
        //   277: istore          9
        //   279: goto            311
        //   282: aload_0        
        //   283: getfield        androidx/work/impl/background/systemjob/SystemJobScheduler.mIdGenerator:Landroidx/work/impl/utils/IdGenerator;
        //   286: aload_0        
        //   287: getfield        androidx/work/impl/background/systemjob/SystemJobScheduler.mWorkManager:Landroidx/work/impl/WorkManagerImpl;
        //   290: invokevirtual   androidx/work/impl/WorkManagerImpl.getConfiguration:()Landroidx/work/Configuration;
        //   293: invokevirtual   androidx/work/Configuration.getMinJobSchedulerId:()I
        //   296: aload_0        
        //   297: getfield        androidx/work/impl/background/systemjob/SystemJobScheduler.mWorkManager:Landroidx/work/impl/WorkManagerImpl;
        //   300: invokevirtual   androidx/work/impl/WorkManagerImpl.getConfiguration:()Landroidx/work/Configuration;
        //   303: invokevirtual   androidx/work/Configuration.getMaxJobSchedulerId:()I
        //   306: invokevirtual   androidx/work/impl/utils/IdGenerator.nextJobSchedulerIdWithRange:(II)I
        //   309: istore          9
        //   311: aload           6
        //   313: ifnonnull       350
        //   316: new             Landroidx/work/impl/model/SystemIdInfo;
        //   319: astore          6
        //   321: aload           6
        //   323: aload           5
        //   325: getfield        androidx/work/impl/model/WorkSpec.id:Ljava/lang/String;
        //   328: iload           9
        //   330: invokespecial   androidx/work/impl/model/SystemIdInfo.<init>:(Ljava/lang/String;I)V
        //   333: aload_0        
        //   334: getfield        androidx/work/impl/background/systemjob/SystemJobScheduler.mWorkManager:Landroidx/work/impl/WorkManagerImpl;
        //   337: invokevirtual   androidx/work/impl/WorkManagerImpl.getWorkDatabase:()Landroidx/work/impl/WorkDatabase;
        //   340: invokevirtual   androidx/work/impl/WorkDatabase.systemIdInfoDao:()Landroidx/work/impl/model/SystemIdInfoDao;
        //   343: aload           6
        //   345: invokeinterface androidx/work/impl/model/SystemIdInfoDao.insertSystemIdInfo:(Landroidx/work/impl/model/SystemIdInfo;)V
        //   350: aload_0        
        //   351: aload           5
        //   353: iload           9
        //   355: invokevirtual   androidx/work/impl/background/systemjob/SystemJobScheduler.scheduleInternal:(Landroidx/work/impl/model/WorkSpec;I)V
        //   358: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   361: bipush          23
        //   363: if_icmpne       399
        //   366: aload_0        
        //   367: aload           5
        //   369: aload_0        
        //   370: getfield        androidx/work/impl/background/systemjob/SystemJobScheduler.mIdGenerator:Landroidx/work/impl/utils/IdGenerator;
        //   373: aload_0        
        //   374: getfield        androidx/work/impl/background/systemjob/SystemJobScheduler.mWorkManager:Landroidx/work/impl/WorkManagerImpl;
        //   377: invokevirtual   androidx/work/impl/WorkManagerImpl.getConfiguration:()Landroidx/work/Configuration;
        //   380: invokevirtual   androidx/work/Configuration.getMinJobSchedulerId:()I
        //   383: aload_0        
        //   384: getfield        androidx/work/impl/background/systemjob/SystemJobScheduler.mWorkManager:Landroidx/work/impl/WorkManagerImpl;
        //   387: invokevirtual   androidx/work/impl/WorkManagerImpl.getConfiguration:()Landroidx/work/Configuration;
        //   390: invokevirtual   androidx/work/Configuration.getMaxJobSchedulerId:()I
        //   393: invokevirtual   androidx/work/impl/utils/IdGenerator.nextJobSchedulerIdWithRange:(II)I
        //   396: invokevirtual   androidx/work/impl/background/systemjob/SystemJobScheduler.scheduleInternal:(Landroidx/work/impl/model/WorkSpec;I)V
        //   399: aload_2        
        //   400: invokevirtual   androidx/work/impl/WorkDatabase.setTransactionSuccessful:()V
        //   403: goto            114
        //   406: iinc            4, 1
        //   409: goto            14
        //   412: astore_1       
        //   413: aload_2        
        //   414: invokevirtual   androidx/work/impl/WorkDatabase.endTransaction:()V
        //   417: aload_1        
        //   418: athrow         
        //   419: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  30     46     412    419    Any
        //  51     114    412    419    Any
        //  121    195    412    419    Any
        //  198    214    412    419    Any
        //  219    264    412    419    Any
        //  272    279    412    419    Any
        //  282    311    412    419    Any
        //  316    350    412    419    Any
        //  350    399    412    419    Any
        //  399    403    412    419    Any
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
    
    public void scheduleInternal(final WorkSpec workSpec, final int i) {
        final JobInfo convert = this.mSystemJobInfoConverter.convert(workSpec, i);
        Logger.get().debug(SystemJobScheduler.TAG, String.format("Scheduling work ID %s Job ID %s", workSpec.id, i), new Throwable[0]);
        this.mJobScheduler.schedule(convert);
    }
}
