// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

import androidx.work.WorkInfo;
import java.util.List;
import android.database.Cursor;
import java.util.Iterator;
import java.util.Set;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.util.StringUtil;
import java.util.ArrayList;
import android.support.v4.util.ArrayMap;
import androidx.work.Constraints;
import androidx.work.Data;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;

public class WorkSpecDao_Impl implements WorkSpecDao
{
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfWorkSpec;
    private final SharedSQLiteStatement __preparedStmtOfDelete;
    private final SharedSQLiteStatement __preparedStmtOfIncrementWorkSpecRunAttemptCount;
    private final SharedSQLiteStatement __preparedStmtOfMarkWorkSpecScheduled;
    private final SharedSQLiteStatement __preparedStmtOfPruneFinishedWorkWithZeroDependentsIgnoringKeepForAtLeast;
    private final SharedSQLiteStatement __preparedStmtOfResetScheduledState;
    private final SharedSQLiteStatement __preparedStmtOfResetWorkSpecRunAttemptCount;
    private final SharedSQLiteStatement __preparedStmtOfSetOutput;
    private final SharedSQLiteStatement __preparedStmtOfSetPeriodStartTime;
    
    public WorkSpecDao_Impl(final RoomDatabase _db) {
        this.__db = _db;
        this.__insertionAdapterOfWorkSpec = new EntityInsertionAdapter<WorkSpec>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final WorkSpec workSpec) {
                if (workSpec.id == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, workSpec.id);
                }
                supportSQLiteStatement.bindLong(2, WorkTypeConverters.stateToInt(workSpec.state));
                if (workSpec.workerClassName == null) {
                    supportSQLiteStatement.bindNull(3);
                }
                else {
                    supportSQLiteStatement.bindString(3, workSpec.workerClassName);
                }
                if (workSpec.inputMergerClassName == null) {
                    supportSQLiteStatement.bindNull(4);
                }
                else {
                    supportSQLiteStatement.bindString(4, workSpec.inputMergerClassName);
                }
                final byte[] byteArray = Data.toByteArray(workSpec.input);
                if (byteArray == null) {
                    supportSQLiteStatement.bindNull(5);
                }
                else {
                    supportSQLiteStatement.bindBlob(5, byteArray);
                }
                final byte[] byteArray2 = Data.toByteArray(workSpec.output);
                if (byteArray2 == null) {
                    supportSQLiteStatement.bindNull(6);
                }
                else {
                    supportSQLiteStatement.bindBlob(6, byteArray2);
                }
                supportSQLiteStatement.bindLong(7, workSpec.initialDelay);
                supportSQLiteStatement.bindLong(8, workSpec.intervalDuration);
                supportSQLiteStatement.bindLong(9, workSpec.flexDuration);
                supportSQLiteStatement.bindLong(10, workSpec.runAttemptCount);
                supportSQLiteStatement.bindLong(11, WorkTypeConverters.backoffPolicyToInt(workSpec.backoffPolicy));
                supportSQLiteStatement.bindLong(12, workSpec.backoffDelayDuration);
                supportSQLiteStatement.bindLong(13, workSpec.periodStartTime);
                supportSQLiteStatement.bindLong(14, workSpec.minimumRetentionDuration);
                supportSQLiteStatement.bindLong(15, workSpec.scheduleRequestedAt);
                final Constraints constraints = workSpec.constraints;
                if (constraints != null) {
                    supportSQLiteStatement.bindLong(16, WorkTypeConverters.networkTypeToInt(constraints.getRequiredNetworkType()));
                    supportSQLiteStatement.bindLong(17, constraints.requiresCharging() ? 1 : 0);
                    supportSQLiteStatement.bindLong(18, constraints.requiresDeviceIdle() ? 1 : 0);
                    supportSQLiteStatement.bindLong(19, constraints.requiresBatteryNotLow() ? 1 : 0);
                    supportSQLiteStatement.bindLong(20, constraints.requiresStorageNotLow() ? 1 : 0);
                    supportSQLiteStatement.bindLong(21, constraints.getTriggerContentUpdateDelay());
                    supportSQLiteStatement.bindLong(22, constraints.getTriggerMaxContentDelay());
                    final byte[] contentUriTriggersToByteArray = WorkTypeConverters.contentUriTriggersToByteArray(constraints.getContentUriTriggers());
                    if (contentUriTriggersToByteArray == null) {
                        supportSQLiteStatement.bindNull(23);
                    }
                    else {
                        supportSQLiteStatement.bindBlob(23, contentUriTriggersToByteArray);
                    }
                }
                else {
                    supportSQLiteStatement.bindNull(16);
                    supportSQLiteStatement.bindNull(17);
                    supportSQLiteStatement.bindNull(18);
                    supportSQLiteStatement.bindNull(19);
                    supportSQLiteStatement.bindNull(20);
                    supportSQLiteStatement.bindNull(21);
                    supportSQLiteStatement.bindNull(22);
                    supportSQLiteStatement.bindNull(23);
                }
            }
            
            public String createQuery() {
                return "INSERT OR IGNORE INTO `WorkSpec`(`id`,`state`,`worker_class_name`,`input_merger_class_name`,`input`,`output`,`initial_delay`,`interval_duration`,`flex_duration`,`run_attempt_count`,`backoff_policy`,`backoff_delay_duration`,`period_start_time`,`minimum_retention_duration`,`schedule_requested_at`,`required_network_type`,`requires_charging`,`requires_device_idle`,`requires_battery_not_low`,`requires_storage_not_low`,`trigger_content_update_delay`,`trigger_max_content_delay`,`content_uri_triggers`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
        };
        this.__preparedStmtOfDelete = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "DELETE FROM workspec WHERE id=?";
            }
        };
        this.__preparedStmtOfSetOutput = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "UPDATE workspec SET output=? WHERE id=?";
            }
        };
        this.__preparedStmtOfSetPeriodStartTime = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "UPDATE workspec SET period_start_time=? WHERE id=?";
            }
        };
        this.__preparedStmtOfIncrementWorkSpecRunAttemptCount = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "UPDATE workspec SET run_attempt_count=run_attempt_count+1 WHERE id=?";
            }
        };
        this.__preparedStmtOfResetWorkSpecRunAttemptCount = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "UPDATE workspec SET run_attempt_count=0 WHERE id=?";
            }
        };
        this.__preparedStmtOfMarkWorkSpecScheduled = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "UPDATE workspec SET schedule_requested_at=? WHERE id=?";
            }
        };
        this.__preparedStmtOfResetScheduledState = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "UPDATE workspec SET schedule_requested_at=-1 WHERE state NOT IN (2, 3, 5)";
            }
        };
        this.__preparedStmtOfPruneFinishedWorkWithZeroDependentsIgnoringKeepForAtLeast = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "DELETE FROM workspec WHERE state IN (2, 3, 5) AND (SELECT COUNT(*)=0 FROM dependency WHERE     prerequisite_id=id AND     work_spec_id NOT IN         (SELECT id FROM workspec WHERE state IN (2, 3, 5)))";
            }
        };
    }
    
    private void __fetchRelationshipWorkTagAsjavaLangString(final ArrayMap<String, ArrayList<String>> arrayMap) {
        final Set<String> keySet = arrayMap.keySet();
        if (keySet.isEmpty()) {
            return;
        }
        if (arrayMap.size() > 999) {
            ArrayMap<String, ArrayList<String>> arrayMap2 = new ArrayMap<String, ArrayList<String>>(999);
            final int size = arrayMap.size();
            int n = 0;
            int n2 = 0;
        Label_0045:
            while (true) {
                n2 = 0;
                int i = n;
                while (i < size) {
                    arrayMap2.put(arrayMap.keyAt(i), arrayMap.valueAt(i));
                    n = i + 1;
                    final int n3 = n2 + 1;
                    i = n;
                    if ((n2 = n3) == 999) {
                        this.__fetchRelationshipWorkTagAsjavaLangString(arrayMap2);
                        arrayMap2 = new ArrayMap<String, ArrayList<String>>(999);
                        continue Label_0045;
                    }
                }
                break;
            }
            if (n2 > 0) {
                this.__fetchRelationshipWorkTagAsjavaLangString(arrayMap2);
            }
            return;
        }
        final StringBuilder stringBuilder = StringUtil.newStringBuilder();
        stringBuilder.append("SELECT `tag`,`work_spec_id` FROM `WorkTag` WHERE `work_spec_id` IN (");
        final int size2 = keySet.size();
        StringUtil.appendPlaceholders(stringBuilder, size2);
        stringBuilder.append(")");
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(stringBuilder.toString(), size2 + 0);
        final Iterator<String> iterator = keySet.iterator();
        int n4 = 1;
        while (iterator.hasNext()) {
            final String s = iterator.next();
            if (s == null) {
                acquire.bindNull(n4);
            }
            else {
                acquire.bindString(n4, s);
            }
            ++n4;
        }
        final Cursor query = this.__db.query(acquire);
        try {
            final int columnIndex = query.getColumnIndex("work_spec_id");
            if (columnIndex == -1) {
                return;
            }
            while (query.moveToNext()) {
                if (!query.isNull(columnIndex)) {
                    final ArrayList<String> list = arrayMap.get(query.getString(columnIndex));
                    if (list == null) {
                        continue;
                    }
                    list.add(query.getString(0));
                }
            }
        }
        finally {
            query.close();
        }
    }
    
    @Override
    public void delete(final String s) {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfDelete.acquire();
        this.__db.beginTransaction();
        Label_0041: {
            Label_0033: {
                if (s == null) {
                    Label_0071: {
                        try {
                            acquire.bindNull(1);
                            break Label_0041;
                        }
                        finally {
                            break Label_0071;
                        }
                        break Label_0033;
                    }
                    this.__db.endTransaction();
                    this.__preparedStmtOfDelete.release(acquire);
                }
            }
            acquire.bindString(1, s);
        }
        acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfDelete.release(acquire);
    }
    
    @Override
    public List<String> getAllUnfinishedWork() {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM workspec WHERE state NOT IN (2, 3, 5)", 0);
        final Cursor query = this.__db.query(acquire);
        try {
            final ArrayList list = new ArrayList<String>(query.getCount());
            while (query.moveToNext()) {
                list.add(query.getString(0));
            }
            return (List<String>)list;
        }
        finally {
            query.close();
            acquire.release();
        }
    }
    
    @Override
    public List<WorkSpec> getEligibleWorkForScheduling(int columnIndexOrThrow) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM workspec WHERE state=0 AND schedule_requested_at=-1 LIMIT (SELECT MAX(?-COUNT(*), 0) FROM workspec WHERE schedule_requested_at<>-1 AND state NOT IN (2, 3, 5))", 1);
        acquire.bindLong(1, columnIndexOrThrow);
        final Cursor query = this.__db.query(acquire);
        try {
            final int columnIndexOrThrow2 = query.getColumnIndexOrThrow("id");
            final int columnIndexOrThrow3 = query.getColumnIndexOrThrow("state");
            final int columnIndexOrThrow4 = query.getColumnIndexOrThrow("worker_class_name");
            final int columnIndexOrThrow5 = query.getColumnIndexOrThrow("input_merger_class_name");
            final int columnIndexOrThrow6 = query.getColumnIndexOrThrow("input");
            final int columnIndexOrThrow7 = query.getColumnIndexOrThrow("output");
            final int columnIndexOrThrow8 = query.getColumnIndexOrThrow("initial_delay");
            columnIndexOrThrow = query.getColumnIndexOrThrow("interval_duration");
            final int columnIndexOrThrow9 = query.getColumnIndexOrThrow("flex_duration");
            final int columnIndexOrThrow10 = query.getColumnIndexOrThrow("run_attempt_count");
            final int columnIndexOrThrow11 = query.getColumnIndexOrThrow("backoff_policy");
            final int columnIndexOrThrow12 = query.getColumnIndexOrThrow("backoff_delay_duration");
            final int columnIndexOrThrow13 = query.getColumnIndexOrThrow("period_start_time");
            final int columnIndexOrThrow14 = query.getColumnIndexOrThrow("minimum_retention_duration");
            try {
                final int columnIndexOrThrow15 = query.getColumnIndexOrThrow("schedule_requested_at");
                final int columnIndexOrThrow16 = query.getColumnIndexOrThrow("required_network_type");
                final int columnIndexOrThrow17 = query.getColumnIndexOrThrow("requires_charging");
                final int columnIndexOrThrow18 = query.getColumnIndexOrThrow("requires_device_idle");
                final int columnIndexOrThrow19 = query.getColumnIndexOrThrow("requires_battery_not_low");
                final int columnIndexOrThrow20 = query.getColumnIndexOrThrow("requires_storage_not_low");
                final int columnIndexOrThrow21 = query.getColumnIndexOrThrow("trigger_content_update_delay");
                final int columnIndexOrThrow22 = query.getColumnIndexOrThrow("trigger_max_content_delay");
                final int columnIndexOrThrow23 = query.getColumnIndexOrThrow("content_uri_triggers");
                final ArrayList list = new ArrayList<WorkSpec>(query.getCount());
                while (query.moveToNext()) {
                    final String string = query.getString(columnIndexOrThrow2);
                    final String string2 = query.getString(columnIndexOrThrow4);
                    final Constraints constraints = new Constraints();
                    constraints.setRequiredNetworkType(WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow16)));
                    constraints.setRequiresCharging(query.getInt(columnIndexOrThrow17) != 0);
                    constraints.setRequiresDeviceIdle(query.getInt(columnIndexOrThrow18) != 0);
                    constraints.setRequiresBatteryNotLow(query.getInt(columnIndexOrThrow19) != 0);
                    constraints.setRequiresStorageNotLow(query.getInt(columnIndexOrThrow20) != 0);
                    constraints.setTriggerContentUpdateDelay(query.getLong(columnIndexOrThrow21));
                    constraints.setTriggerMaxContentDelay(query.getLong(columnIndexOrThrow22));
                    constraints.setContentUriTriggers(WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow23)));
                    final WorkSpec workSpec = new WorkSpec(string, string2);
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow3));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow5);
                    workSpec.input = Data.fromByteArray(query.getBlob(columnIndexOrThrow6));
                    workSpec.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow7));
                    workSpec.initialDelay = query.getLong(columnIndexOrThrow8);
                    workSpec.intervalDuration = query.getLong(columnIndexOrThrow);
                    workSpec.flexDuration = query.getLong(columnIndexOrThrow9);
                    workSpec.runAttemptCount = query.getInt(columnIndexOrThrow10);
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(columnIndexOrThrow11));
                    workSpec.backoffDelayDuration = query.getLong(columnIndexOrThrow12);
                    workSpec.periodStartTime = query.getLong(columnIndexOrThrow13);
                    workSpec.minimumRetentionDuration = query.getLong(columnIndexOrThrow14);
                    workSpec.scheduleRequestedAt = query.getLong(columnIndexOrThrow15);
                    workSpec.constraints = constraints;
                    list.add(workSpec);
                }
                query.close();
                acquire.release();
                return (List<WorkSpec>)list;
            }
            finally {}
        }
        finally {}
        query.close();
        acquire.release();
    }
    
    @Override
    public List<Data> getInputsFromPrerequisites(String query) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT output FROM workspec WHERE id IN (SELECT prerequisite_id FROM dependency WHERE work_spec_id=?)", 1);
        if (query == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, query);
        }
        query = (String)this.__db.query(acquire);
        try {
            final ArrayList<Data> list = new ArrayList<Data>(((Cursor)query).getCount());
            while (((Cursor)query).moveToNext()) {
                list.add(Data.fromByteArray(((Cursor)query).getBlob(0)));
            }
            return list;
        }
        finally {
            ((Cursor)query).close();
            acquire.release();
        }
    }
    
    @Override
    public List<WorkSpec> getScheduledWork() {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM workspec WHERE state=0 AND schedule_requested_at<>-1", 0);
        final Cursor query = this.__db.query(acquire);
        try {
            final int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            final int columnIndexOrThrow2 = query.getColumnIndexOrThrow("state");
            final int columnIndexOrThrow3 = query.getColumnIndexOrThrow("worker_class_name");
            final int columnIndexOrThrow4 = query.getColumnIndexOrThrow("input_merger_class_name");
            final int columnIndexOrThrow5 = query.getColumnIndexOrThrow("input");
            final int columnIndexOrThrow6 = query.getColumnIndexOrThrow("output");
            final int columnIndexOrThrow7 = query.getColumnIndexOrThrow("initial_delay");
            final int columnIndexOrThrow8 = query.getColumnIndexOrThrow("interval_duration");
            final int columnIndexOrThrow9 = query.getColumnIndexOrThrow("flex_duration");
            final int columnIndexOrThrow10 = query.getColumnIndexOrThrow("run_attempt_count");
            final int columnIndexOrThrow11 = query.getColumnIndexOrThrow("backoff_policy");
            final int columnIndexOrThrow12 = query.getColumnIndexOrThrow("backoff_delay_duration");
            final int columnIndexOrThrow13 = query.getColumnIndexOrThrow("period_start_time");
            final int columnIndexOrThrow14 = query.getColumnIndexOrThrow("minimum_retention_duration");
            try {
                final int columnIndexOrThrow15 = query.getColumnIndexOrThrow("schedule_requested_at");
                final int columnIndexOrThrow16 = query.getColumnIndexOrThrow("required_network_type");
                final int columnIndexOrThrow17 = query.getColumnIndexOrThrow("requires_charging");
                final int columnIndexOrThrow18 = query.getColumnIndexOrThrow("requires_device_idle");
                final int columnIndexOrThrow19 = query.getColumnIndexOrThrow("requires_battery_not_low");
                final int columnIndexOrThrow20 = query.getColumnIndexOrThrow("requires_storage_not_low");
                final int columnIndexOrThrow21 = query.getColumnIndexOrThrow("trigger_content_update_delay");
                final int columnIndexOrThrow22 = query.getColumnIndexOrThrow("trigger_max_content_delay");
                final int columnIndexOrThrow23 = query.getColumnIndexOrThrow("content_uri_triggers");
                final ArrayList list = new ArrayList<WorkSpec>(query.getCount());
                while (query.moveToNext()) {
                    final String string = query.getString(columnIndexOrThrow);
                    final String string2 = query.getString(columnIndexOrThrow3);
                    final Constraints constraints = new Constraints();
                    constraints.setRequiredNetworkType(WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow16)));
                    constraints.setRequiresCharging(query.getInt(columnIndexOrThrow17) != 0);
                    constraints.setRequiresDeviceIdle(query.getInt(columnIndexOrThrow18) != 0);
                    constraints.setRequiresBatteryNotLow(query.getInt(columnIndexOrThrow19) != 0);
                    constraints.setRequiresStorageNotLow(query.getInt(columnIndexOrThrow20) != 0);
                    constraints.setTriggerContentUpdateDelay(query.getLong(columnIndexOrThrow21));
                    constraints.setTriggerMaxContentDelay(query.getLong(columnIndexOrThrow22));
                    constraints.setContentUriTriggers(WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow23)));
                    final WorkSpec workSpec = new WorkSpec(string, string2);
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow2));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow4);
                    workSpec.input = Data.fromByteArray(query.getBlob(columnIndexOrThrow5));
                    workSpec.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow6));
                    workSpec.initialDelay = query.getLong(columnIndexOrThrow7);
                    workSpec.intervalDuration = query.getLong(columnIndexOrThrow8);
                    workSpec.flexDuration = query.getLong(columnIndexOrThrow9);
                    workSpec.runAttemptCount = query.getInt(columnIndexOrThrow10);
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(columnIndexOrThrow11));
                    workSpec.backoffDelayDuration = query.getLong(columnIndexOrThrow12);
                    workSpec.periodStartTime = query.getLong(columnIndexOrThrow13);
                    workSpec.minimumRetentionDuration = query.getLong(columnIndexOrThrow14);
                    workSpec.scheduleRequestedAt = query.getLong(columnIndexOrThrow15);
                    workSpec.constraints = constraints;
                    list.add(workSpec);
                }
                query.close();
                acquire.release();
                return (List<WorkSpec>)list;
            }
            finally {}
        }
        finally {}
        query.close();
        acquire.release();
    }
    
    @Override
    public WorkInfo.State getState(final String s) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT state FROM workspec WHERE id=?", 1);
        if (s == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, s);
        }
        final Cursor query = this.__db.query(acquire);
        try {
            WorkInfo.State intToState;
            if (query.moveToFirst()) {
                intToState = WorkTypeConverters.intToState(query.getInt(0));
            }
            else {
                intToState = null;
            }
            return intToState;
        }
        finally {
            query.close();
            acquire.release();
        }
    }
    
    @Override
    public List<String> getUnfinishedWorkWithName(String query) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM workspec WHERE state NOT IN (2, 3, 5) AND id IN (SELECT work_spec_id FROM workname WHERE name=?)", 1);
        if (query == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, query);
        }
        query = (String)this.__db.query(acquire);
        try {
            final ArrayList<String> list = new ArrayList<String>(((Cursor)query).getCount());
            while (((Cursor)query).moveToNext()) {
                list.add(((Cursor)query).getString(0));
            }
            return list;
        }
        finally {
            ((Cursor)query).close();
            acquire.release();
        }
    }
    
    @Override
    public List<String> getUnfinishedWorkWithTag(String query) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM workspec WHERE state NOT IN (2, 3, 5) AND id IN (SELECT work_spec_id FROM worktag WHERE tag=?)", 1);
        if (query == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, query);
        }
        query = (String)this.__db.query(acquire);
        try {
            final ArrayList<String> list = new ArrayList<String>(((Cursor)query).getCount());
            while (((Cursor)query).moveToNext()) {
                list.add(((Cursor)query).getString(0));
            }
            return list;
        }
        finally {
            ((Cursor)query).close();
            acquire.release();
        }
    }
    
    @Override
    public WorkSpec getWorkSpec(final String s) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM workspec WHERE id=?", 1);
        if (s == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, s);
        }
        final Cursor query = this.__db.query(acquire);
        try {
            final int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            final int columnIndexOrThrow2 = query.getColumnIndexOrThrow("state");
            final int columnIndexOrThrow3 = query.getColumnIndexOrThrow("worker_class_name");
            final int columnIndexOrThrow4 = query.getColumnIndexOrThrow("input_merger_class_name");
            final int columnIndexOrThrow5 = query.getColumnIndexOrThrow("input");
            final int columnIndexOrThrow6 = query.getColumnIndexOrThrow("output");
            final int columnIndexOrThrow7 = query.getColumnIndexOrThrow("initial_delay");
            final int columnIndexOrThrow8 = query.getColumnIndexOrThrow("interval_duration");
            final int columnIndexOrThrow9 = query.getColumnIndexOrThrow("flex_duration");
            final int columnIndexOrThrow10 = query.getColumnIndexOrThrow("run_attempt_count");
            final int columnIndexOrThrow11 = query.getColumnIndexOrThrow("backoff_policy");
            final int columnIndexOrThrow12 = query.getColumnIndexOrThrow("backoff_delay_duration");
            final int columnIndexOrThrow13 = query.getColumnIndexOrThrow("period_start_time");
            final int columnIndexOrThrow14 = query.getColumnIndexOrThrow("minimum_retention_duration");
            try {
                final int columnIndexOrThrow15 = query.getColumnIndexOrThrow("schedule_requested_at");
                final int columnIndexOrThrow16 = query.getColumnIndexOrThrow("required_network_type");
                final int columnIndexOrThrow17 = query.getColumnIndexOrThrow("requires_charging");
                final int columnIndexOrThrow18 = query.getColumnIndexOrThrow("requires_device_idle");
                final int columnIndexOrThrow19 = query.getColumnIndexOrThrow("requires_battery_not_low");
                final int columnIndexOrThrow20 = query.getColumnIndexOrThrow("requires_storage_not_low");
                final int columnIndexOrThrow21 = query.getColumnIndexOrThrow("trigger_content_update_delay");
                final int columnIndexOrThrow22 = query.getColumnIndexOrThrow("trigger_max_content_delay");
                final int columnIndexOrThrow23 = query.getColumnIndexOrThrow("content_uri_triggers");
                WorkSpec workSpec;
                if (query.moveToFirst()) {
                    final String string = query.getString(columnIndexOrThrow);
                    final String string2 = query.getString(columnIndexOrThrow3);
                    final Constraints constraints = new Constraints();
                    constraints.setRequiredNetworkType(WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow16)));
                    final int int1 = query.getInt(columnIndexOrThrow17);
                    final boolean b = false;
                    constraints.setRequiresCharging(int1 != 0);
                    constraints.setRequiresDeviceIdle(query.getInt(columnIndexOrThrow18) != 0);
                    constraints.setRequiresBatteryNotLow(query.getInt(columnIndexOrThrow19) != 0);
                    boolean requiresStorageNotLow = b;
                    if (query.getInt(columnIndexOrThrow20) != 0) {
                        requiresStorageNotLow = true;
                    }
                    constraints.setRequiresStorageNotLow(requiresStorageNotLow);
                    constraints.setTriggerContentUpdateDelay(query.getLong(columnIndexOrThrow21));
                    constraints.setTriggerMaxContentDelay(query.getLong(columnIndexOrThrow22));
                    constraints.setContentUriTriggers(WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow23)));
                    workSpec = new WorkSpec(string, string2);
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow2));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow4);
                    workSpec.input = Data.fromByteArray(query.getBlob(columnIndexOrThrow5));
                    workSpec.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow6));
                    workSpec.initialDelay = query.getLong(columnIndexOrThrow7);
                    workSpec.intervalDuration = query.getLong(columnIndexOrThrow8);
                    workSpec.flexDuration = query.getLong(columnIndexOrThrow9);
                    workSpec.runAttemptCount = query.getInt(columnIndexOrThrow10);
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(columnIndexOrThrow11));
                    workSpec.backoffDelayDuration = query.getLong(columnIndexOrThrow12);
                    workSpec.periodStartTime = query.getLong(columnIndexOrThrow13);
                    workSpec.minimumRetentionDuration = query.getLong(columnIndexOrThrow14);
                    workSpec.scheduleRequestedAt = query.getLong(columnIndexOrThrow15);
                    workSpec.constraints = constraints;
                }
                else {
                    workSpec = null;
                }
                query.close();
                acquire.release();
                return workSpec;
            }
            finally {}
        }
        finally {}
        query.close();
        acquire.release();
    }
    
    @Override
    public List<WorkSpec.IdAndState> getWorkSpecIdAndStatesForName(String query) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, state FROM workspec WHERE id IN (SELECT work_spec_id FROM workname WHERE name=?)", 1);
        if (query == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, query);
        }
        query = (String)this.__db.query(acquire);
        try {
            final int columnIndexOrThrow = ((Cursor)query).getColumnIndexOrThrow("id");
            final int columnIndexOrThrow2 = ((Cursor)query).getColumnIndexOrThrow("state");
            final ArrayList<WorkSpec.IdAndState> list = new ArrayList<WorkSpec.IdAndState>(((Cursor)query).getCount());
            while (((Cursor)query).moveToNext()) {
                final WorkSpec.IdAndState idAndState = new WorkSpec.IdAndState();
                idAndState.id = ((Cursor)query).getString(columnIndexOrThrow);
                idAndState.state = WorkTypeConverters.intToState(((Cursor)query).getInt(columnIndexOrThrow2));
                list.add(idAndState);
            }
            return list;
        }
        finally {
            ((Cursor)query).close();
            acquire.release();
        }
    }
    
    @Override
    public List<WorkSpec.WorkInfoPojo> getWorkStatusPojoForTag(final String s) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, state, output FROM workspec WHERE id IN (SELECT work_spec_id FROM worktag WHERE tag=?)", 1);
        if (s == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, s);
        }
        this.__db.beginTransaction();
        try {
            final Cursor query = this.__db.query(acquire);
            try {
                final ArrayMap<String, ArrayList<String>> arrayMap = (ArrayMap<String, ArrayList<String>>)new ArrayMap<Object, ArrayList<String>>();
                final int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
                final int columnIndexOrThrow2 = query.getColumnIndexOrThrow("state");
                final int columnIndexOrThrow3 = query.getColumnIndexOrThrow("output");
                final ArrayList list = new ArrayList<WorkSpec.WorkInfoPojo>(query.getCount());
                while (query.moveToNext()) {
                    final WorkSpec.WorkInfoPojo workInfoPojo = new WorkSpec.WorkInfoPojo();
                    workInfoPojo.id = query.getString(columnIndexOrThrow);
                    workInfoPojo.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow2));
                    workInfoPojo.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow3));
                    if (!query.isNull(columnIndexOrThrow)) {
                        final String string = query.getString(columnIndexOrThrow);
                        ArrayList<String> tags;
                        if ((tags = arrayMap.get(string)) == null) {
                            tags = new ArrayList<String>();
                            arrayMap.put(string, tags);
                        }
                        workInfoPojo.tags = tags;
                    }
                    list.add(workInfoPojo);
                }
                this.__fetchRelationshipWorkTagAsjavaLangString(arrayMap);
                this.__db.setTransactionSuccessful();
                return (List<WorkSpec.WorkInfoPojo>)list;
            }
            finally {
                query.close();
                acquire.release();
            }
        }
        finally {
            this.__db.endTransaction();
        }
    }
    
    @Override
    public int incrementWorkSpecRunAttemptCount(final String s) {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfIncrementWorkSpecRunAttemptCount.acquire();
        this.__db.beginTransaction();
        Label_0041: {
            Label_0033: {
                if (s == null) {
                    Label_0072: {
                        try {
                            acquire.bindNull(1);
                            break Label_0041;
                        }
                        finally {
                            break Label_0072;
                        }
                        break Label_0033;
                    }
                    this.__db.endTransaction();
                    this.__preparedStmtOfIncrementWorkSpecRunAttemptCount.release(acquire);
                }
            }
            acquire.bindString(1, s);
        }
        final int executeUpdateDelete = acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfIncrementWorkSpecRunAttemptCount.release(acquire);
        return executeUpdateDelete;
    }
    
    @Override
    public void insertWorkSpec(final WorkSpec workSpec) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfWorkSpec.insert(workSpec);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
    
    @Override
    public int markWorkSpecScheduled(final String s, final long n) {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfMarkWorkSpecScheduled.acquire();
        this.__db.beginTransaction();
        try {
            acquire.bindLong(1, n);
            if (s == null) {
                acquire.bindNull(2);
            }
            else {
                acquire.bindString(2, s);
            }
            final int executeUpdateDelete = acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        }
        finally {
            this.__db.endTransaction();
            this.__preparedStmtOfMarkWorkSpecScheduled.release(acquire);
        }
    }
    
    @Override
    public int resetScheduledState() {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfResetScheduledState.acquire();
        this.__db.beginTransaction();
        try {
            final int executeUpdateDelete = acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        }
        finally {
            this.__db.endTransaction();
            this.__preparedStmtOfResetScheduledState.release(acquire);
        }
    }
    
    @Override
    public int resetWorkSpecRunAttemptCount(final String s) {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfResetWorkSpecRunAttemptCount.acquire();
        this.__db.beginTransaction();
        Label_0041: {
            Label_0033: {
                if (s == null) {
                    Label_0072: {
                        try {
                            acquire.bindNull(1);
                            break Label_0041;
                        }
                        finally {
                            break Label_0072;
                        }
                        break Label_0033;
                    }
                    this.__db.endTransaction();
                    this.__preparedStmtOfResetWorkSpecRunAttemptCount.release(acquire);
                }
            }
            acquire.bindString(1, s);
        }
        final int executeUpdateDelete = acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfResetWorkSpecRunAttemptCount.release(acquire);
        return executeUpdateDelete;
    }
    
    @Override
    public void setOutput(final String s, final Data data) {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfSetOutput.acquire();
        this.__db.beginTransaction();
        try {
            final byte[] byteArray = Data.toByteArray(data);
            if (byteArray == null) {
                acquire.bindNull(1);
            }
            else {
                acquire.bindBlob(1, byteArray);
            }
            if (s == null) {
                acquire.bindNull(2);
            }
            else {
                acquire.bindString(2, s);
            }
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
            this.__preparedStmtOfSetOutput.release(acquire);
        }
    }
    
    @Override
    public void setPeriodStartTime(final String s, final long n) {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfSetPeriodStartTime.acquire();
        this.__db.beginTransaction();
        try {
            acquire.bindLong(1, n);
            if (s == null) {
                acquire.bindNull(2);
            }
            else {
                acquire.bindString(2, s);
            }
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
            this.__preparedStmtOfSetPeriodStartTime.release(acquire);
        }
    }
    
    @Override
    public int setState(final WorkInfo.State state, final String... array) {
        final StringBuilder stringBuilder = StringUtil.newStringBuilder();
        stringBuilder.append("UPDATE workspec SET state=");
        stringBuilder.append("?");
        stringBuilder.append(" WHERE id IN (");
        StringUtil.appendPlaceholders(stringBuilder, array.length);
        stringBuilder.append(")");
        final SupportSQLiteStatement compileStatement = this.__db.compileStatement(stringBuilder.toString());
        compileStatement.bindLong(1, WorkTypeConverters.stateToInt(state));
        final int length = array.length;
        int n = 2;
        for (final String s : array) {
            if (s == null) {
                compileStatement.bindNull(n);
            }
            else {
                compileStatement.bindString(n, s);
            }
            ++n;
        }
        this.__db.beginTransaction();
        try {
            final int executeUpdateDelete = compileStatement.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        }
        finally {
            this.__db.endTransaction();
        }
    }
}
