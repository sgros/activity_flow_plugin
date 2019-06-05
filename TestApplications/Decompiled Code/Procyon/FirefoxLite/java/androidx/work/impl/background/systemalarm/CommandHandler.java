// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import androidx.work.impl.WorkDatabase;
import android.arch.persistence.room.RoomDatabase;
import androidx.work.impl.model.WorkSpec;
import android.os.Bundle;
import android.content.Intent;
import java.util.HashMap;
import androidx.work.Logger;
import java.util.Map;
import android.content.Context;
import androidx.work.impl.ExecutionListener;

public class CommandHandler implements ExecutionListener
{
    private static final String TAG;
    private final Context mContext;
    private final Object mLock;
    private final Map<String, ExecutionListener> mPendingDelayMet;
    
    static {
        TAG = Logger.tagWithPrefix("CommandHandler");
    }
    
    CommandHandler(final Context mContext) {
        this.mContext = mContext;
        this.mPendingDelayMet = new HashMap<String, ExecutionListener>();
        this.mLock = new Object();
    }
    
    static Intent createConstraintsChangedIntent(final Context context) {
        final Intent intent = new Intent(context, (Class)SystemAlarmService.class);
        intent.setAction("ACTION_CONSTRAINTS_CHANGED");
        return intent;
    }
    
    static Intent createDelayMetIntent(final Context context, final String s) {
        final Intent intent = new Intent(context, (Class)SystemAlarmService.class);
        intent.setAction("ACTION_DELAY_MET");
        intent.putExtra("KEY_WORKSPEC_ID", s);
        return intent;
    }
    
    static Intent createExecutionCompletedIntent(final Context context, final String s, final boolean b) {
        final Intent intent = new Intent(context, (Class)SystemAlarmService.class);
        intent.setAction("ACTION_EXECUTION_COMPLETED");
        intent.putExtra("KEY_WORKSPEC_ID", s);
        intent.putExtra("KEY_NEEDS_RESCHEDULE", b);
        return intent;
    }
    
    static Intent createRescheduleIntent(final Context context) {
        final Intent intent = new Intent(context, (Class)SystemAlarmService.class);
        intent.setAction("ACTION_RESCHEDULE");
        return intent;
    }
    
    static Intent createScheduleWorkIntent(final Context context, final String s) {
        final Intent intent = new Intent(context, (Class)SystemAlarmService.class);
        intent.setAction("ACTION_SCHEDULE_WORK");
        intent.putExtra("KEY_WORKSPEC_ID", s);
        return intent;
    }
    
    static Intent createStopWorkIntent(final Context context, final String s) {
        final Intent intent = new Intent(context, (Class)SystemAlarmService.class);
        intent.setAction("ACTION_STOP_WORK");
        intent.putExtra("KEY_WORKSPEC_ID", s);
        return intent;
    }
    
    private void handleConstraintsChanged(final Intent intent, final int n, final SystemAlarmDispatcher systemAlarmDispatcher) {
        Logger.get().debug(CommandHandler.TAG, String.format("Handling constraints changed %s", intent), new Throwable[0]);
        new ConstraintsCommandHandler(this.mContext, n, systemAlarmDispatcher).handleConstraintsChanged();
    }
    
    private void handleDelayMet(final Intent intent, final int n, final SystemAlarmDispatcher systemAlarmDispatcher) {
        final Bundle extras = intent.getExtras();
        synchronized (this.mLock) {
            final String string = extras.getString("KEY_WORKSPEC_ID");
            Logger.get().debug(CommandHandler.TAG, String.format("Handing delay met for %s", string), new Throwable[0]);
            if (!this.mPendingDelayMet.containsKey(string)) {
                final DelayMetCommandHandler delayMetCommandHandler = new DelayMetCommandHandler(this.mContext, n, string, systemAlarmDispatcher);
                this.mPendingDelayMet.put(string, delayMetCommandHandler);
                delayMetCommandHandler.handleProcessWork();
            }
            else {
                Logger.get().debug(CommandHandler.TAG, String.format("WorkSpec %s is already being handled for ACTION_DELAY_MET", string), new Throwable[0]);
            }
        }
    }
    
    private void handleExecutionCompleted(final Intent intent, final int i, final SystemAlarmDispatcher systemAlarmDispatcher) {
        final Bundle extras = intent.getExtras();
        final String string = extras.getString("KEY_WORKSPEC_ID");
        final boolean boolean1 = extras.getBoolean("KEY_NEEDS_RESCHEDULE");
        Logger.get().debug(CommandHandler.TAG, String.format("Handling onExecutionCompleted %s, %s", intent, i), new Throwable[0]);
        this.onExecuted(string, boolean1);
    }
    
    private void handleReschedule(final Intent intent, final int i, final SystemAlarmDispatcher systemAlarmDispatcher) {
        Logger.get().debug(CommandHandler.TAG, String.format("Handling reschedule %s, %s", intent, i), new Throwable[0]);
        systemAlarmDispatcher.getWorkManager().rescheduleEligibleWork();
    }
    
    private void handleScheduleWorkIntent(Intent workDatabase, final int n, final SystemAlarmDispatcher systemAlarmDispatcher) {
        final String string = workDatabase.getExtras().getString("KEY_WORKSPEC_ID");
        Logger.get().debug(CommandHandler.TAG, String.format("Handling schedule work for %s", string), new Throwable[0]);
        workDatabase = (Intent)systemAlarmDispatcher.getWorkManager().getWorkDatabase();
        ((RoomDatabase)workDatabase).beginTransaction();
        try {
            final WorkSpec workSpec = ((WorkDatabase)workDatabase).workSpecDao().getWorkSpec(string);
            if (workSpec == null) {
                final Logger value = Logger.get();
                final String tag = CommandHandler.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Skipping scheduling ");
                sb.append(string);
                sb.append(" because it's no longer in the DB");
                value.warning(tag, sb.toString(), new Throwable[0]);
                return;
            }
            if (workSpec.state.isFinished()) {
                final Logger value2 = Logger.get();
                final String tag2 = CommandHandler.TAG;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Skipping scheduling ");
                sb2.append(string);
                sb2.append("because it is finished.");
                value2.warning(tag2, sb2.toString(), new Throwable[0]);
                return;
            }
            final long calculateNextRunTime = workSpec.calculateNextRunTime();
            if (!workSpec.hasConstraints()) {
                Logger.get().debug(CommandHandler.TAG, String.format("Setting up Alarms for %s at %s", string, calculateNextRunTime), new Throwable[0]);
                Alarms.setAlarm(this.mContext, systemAlarmDispatcher.getWorkManager(), string, calculateNextRunTime);
            }
            else {
                Logger.get().debug(CommandHandler.TAG, String.format("Opportunistically setting an alarm for %s at %s", string, calculateNextRunTime), new Throwable[0]);
                Alarms.setAlarm(this.mContext, systemAlarmDispatcher.getWorkManager(), string, calculateNextRunTime);
                systemAlarmDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(systemAlarmDispatcher, createConstraintsChangedIntent(this.mContext), n));
            }
            ((RoomDatabase)workDatabase).setTransactionSuccessful();
        }
        finally {
            ((RoomDatabase)workDatabase).endTransaction();
        }
    }
    
    private void handleStopWork(final Intent intent, final int n, final SystemAlarmDispatcher systemAlarmDispatcher) {
        final String string = intent.getExtras().getString("KEY_WORKSPEC_ID");
        Logger.get().debug(CommandHandler.TAG, String.format("Handing stopWork work for %s", string), new Throwable[0]);
        systemAlarmDispatcher.getWorkManager().stopWork(string);
        Alarms.cancelAlarm(this.mContext, systemAlarmDispatcher.getWorkManager(), string);
        systemAlarmDispatcher.onExecuted(string, false);
    }
    
    private static boolean hasKeys(final Bundle bundle, final String... array) {
        if (bundle != null && !bundle.isEmpty()) {
            for (int length = array.length, i = 0; i < length; ++i) {
                if (bundle.get(array[i]) == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    boolean hasPendingCommands() {
        synchronized (this.mLock) {
            final boolean empty = this.mPendingDelayMet.isEmpty();
            // monitorexit(this.mLock)
            return empty ^ true;
        }
    }
    
    @Override
    public void onExecuted(final String s, final boolean b) {
        synchronized (this.mLock) {
            final ExecutionListener executionListener = this.mPendingDelayMet.remove(s);
            if (executionListener != null) {
                executionListener.onExecuted(s, b);
            }
        }
    }
    
    void onHandleIntent(final Intent intent, final int n, final SystemAlarmDispatcher systemAlarmDispatcher) {
        final String action = intent.getAction();
        if ("ACTION_CONSTRAINTS_CHANGED".equals(action)) {
            this.handleConstraintsChanged(intent, n, systemAlarmDispatcher);
        }
        else if ("ACTION_RESCHEDULE".equals(action)) {
            this.handleReschedule(intent, n, systemAlarmDispatcher);
        }
        else if (!hasKeys(intent.getExtras(), "KEY_WORKSPEC_ID")) {
            Logger.get().error(CommandHandler.TAG, String.format("Invalid request for %s, requires %s.", action, "KEY_WORKSPEC_ID"), new Throwable[0]);
        }
        else if ("ACTION_SCHEDULE_WORK".equals(action)) {
            this.handleScheduleWorkIntent(intent, n, systemAlarmDispatcher);
        }
        else if ("ACTION_DELAY_MET".equals(action)) {
            this.handleDelayMet(intent, n, systemAlarmDispatcher);
        }
        else if ("ACTION_STOP_WORK".equals(action)) {
            this.handleStopWork(intent, n, systemAlarmDispatcher);
        }
        else if ("ACTION_EXECUTION_COMPLETED".equals(action)) {
            this.handleExecutionCompleted(intent, n, systemAlarmDispatcher);
        }
        else {
            Logger.get().warning(CommandHandler.TAG, String.format("Ignoring intent %s", intent), new Throwable[0]);
        }
    }
}
