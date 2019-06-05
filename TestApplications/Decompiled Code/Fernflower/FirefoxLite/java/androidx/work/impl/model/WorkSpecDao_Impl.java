package androidx.work.impl.model;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.arch.persistence.room.util.StringUtil;
import android.database.Cursor;
import android.support.v4.util.ArrayMap;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.WorkInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WorkSpecDao_Impl implements WorkSpecDao {
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

   public WorkSpecDao_Impl(RoomDatabase var1) {
      this.__db = var1;
      this.__insertionAdapterOfWorkSpec = new EntityInsertionAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, WorkSpec var2) {
            if (var2.id == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.id);
            }

            var1.bindLong(2, (long)WorkTypeConverters.stateToInt(var2.state));
            if (var2.workerClassName == null) {
               var1.bindNull(3);
            } else {
               var1.bindString(3, var2.workerClassName);
            }

            if (var2.inputMergerClassName == null) {
               var1.bindNull(4);
            } else {
               var1.bindString(4, var2.inputMergerClassName);
            }

            byte[] var3 = Data.toByteArray(var2.input);
            if (var3 == null) {
               var1.bindNull(5);
            } else {
               var1.bindBlob(5, var3);
            }

            var3 = Data.toByteArray(var2.output);
            if (var3 == null) {
               var1.bindNull(6);
            } else {
               var1.bindBlob(6, var3);
            }

            var1.bindLong(7, var2.initialDelay);
            var1.bindLong(8, var2.intervalDuration);
            var1.bindLong(9, var2.flexDuration);
            var1.bindLong(10, (long)var2.runAttemptCount);
            var1.bindLong(11, (long)WorkTypeConverters.backoffPolicyToInt(var2.backoffPolicy));
            var1.bindLong(12, var2.backoffDelayDuration);
            var1.bindLong(13, var2.periodStartTime);
            var1.bindLong(14, var2.minimumRetentionDuration);
            var1.bindLong(15, var2.scheduleRequestedAt);
            Constraints var4 = var2.constraints;
            if (var4 != null) {
               var1.bindLong(16, (long)WorkTypeConverters.networkTypeToInt(var4.getRequiredNetworkType()));
               var1.bindLong(17, (long)var4.requiresCharging());
               var1.bindLong(18, (long)var4.requiresDeviceIdle());
               var1.bindLong(19, (long)var4.requiresBatteryNotLow());
               var1.bindLong(20, (long)var4.requiresStorageNotLow());
               var1.bindLong(21, var4.getTriggerContentUpdateDelay());
               var1.bindLong(22, var4.getTriggerMaxContentDelay());
               byte[] var5 = WorkTypeConverters.contentUriTriggersToByteArray(var4.getContentUriTriggers());
               if (var5 == null) {
                  var1.bindNull(23);
               } else {
                  var1.bindBlob(23, var5);
               }
            } else {
               var1.bindNull(16);
               var1.bindNull(17);
               var1.bindNull(18);
               var1.bindNull(19);
               var1.bindNull(20);
               var1.bindNull(21);
               var1.bindNull(22);
               var1.bindNull(23);
            }

         }

         public String createQuery() {
            return "INSERT OR IGNORE INTO `WorkSpec`(`id`,`state`,`worker_class_name`,`input_merger_class_name`,`input`,`output`,`initial_delay`,`interval_duration`,`flex_duration`,`run_attempt_count`,`backoff_policy`,`backoff_delay_duration`,`period_start_time`,`minimum_retention_duration`,`schedule_requested_at`,`required_network_type`,`requires_charging`,`requires_device_idle`,`requires_battery_not_low`,`requires_storage_not_low`,`trigger_content_update_delay`,`trigger_max_content_delay`,`content_uri_triggers`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
         }
      };
      this.__preparedStmtOfDelete = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "DELETE FROM workspec WHERE id=?";
         }
      };
      this.__preparedStmtOfSetOutput = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "UPDATE workspec SET output=? WHERE id=?";
         }
      };
      this.__preparedStmtOfSetPeriodStartTime = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "UPDATE workspec SET period_start_time=? WHERE id=?";
         }
      };
      this.__preparedStmtOfIncrementWorkSpecRunAttemptCount = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "UPDATE workspec SET run_attempt_count=run_attempt_count+1 WHERE id=?";
         }
      };
      this.__preparedStmtOfResetWorkSpecRunAttemptCount = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "UPDATE workspec SET run_attempt_count=0 WHERE id=?";
         }
      };
      this.__preparedStmtOfMarkWorkSpecScheduled = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "UPDATE workspec SET schedule_requested_at=? WHERE id=?";
         }
      };
      this.__preparedStmtOfResetScheduledState = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "UPDATE workspec SET schedule_requested_at=-1 WHERE state NOT IN (2, 3, 5)";
         }
      };
      this.__preparedStmtOfPruneFinishedWorkWithZeroDependentsIgnoringKeepForAtLeast = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "DELETE FROM workspec WHERE state IN (2, 3, 5) AND (SELECT COUNT(*)=0 FROM dependency WHERE     prerequisite_id=id AND     work_spec_id NOT IN         (SELECT id FROM workspec WHERE state IN (2, 3, 5)))";
         }
      };
   }

   private void __fetchRelationshipWorkTagAsjavaLangString(ArrayMap var1) {
      Set var2 = var1.keySet();
      if (!var2.isEmpty()) {
         int var5;
         if (var1.size() > 999) {
            ArrayMap var27 = new ArrayMap(999);
            int var4 = var1.size();
            var5 = 0;

            while(true) {
               int var6 = 0;
               int var7 = var5;

               int var8;
               do {
                  if (var7 >= var4) {
                     if (var6 > 0) {
                        this.__fetchRelationshipWorkTagAsjavaLangString(var27);
                     }

                     return;
                  }

                  var27.put(var1.keyAt(var7), var1.valueAt(var7));
                  var5 = var7 + 1;
                  var8 = var6 + 1;
                  var7 = var5;
                  var6 = var8;
               } while(var8 != 999);

               this.__fetchRelationshipWorkTagAsjavaLangString(var27);
               var27 = new ArrayMap(999);
            }
         } else {
            StringBuilder var3 = StringUtil.newStringBuilder();
            var3.append("SELECT `tag`,`work_spec_id` FROM `WorkTag` WHERE `work_spec_id` IN (");
            var5 = var2.size();
            StringUtil.appendPlaceholders(var3, var5);
            var3.append(")");
            RoomSQLiteQuery var25 = RoomSQLiteQuery.acquire(var3.toString(), var5 + 0);
            Iterator var23 = var2.iterator();

            for(var5 = 1; var23.hasNext(); ++var5) {
               String var9 = (String)var23.next();
               if (var9 == null) {
                  var25.bindNull(var5);
               } else {
                  var25.bindString(var5, var9);
               }
            }

            Cursor var26 = this.__db.query(var25);

            label335: {
               Throwable var10000;
               label347: {
                  boolean var10001;
                  try {
                     var5 = var26.getColumnIndex("work_spec_id");
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label347;
                  }

                  if (var5 == -1) {
                     var26.close();
                     return;
                  }

                  while(true) {
                     ArrayList var24;
                     try {
                        do {
                           if (!var26.moveToNext()) {
                              break label335;
                           }
                        } while(var26.isNull(var5));

                        var24 = (ArrayList)var1.get(var26.getString(var5));
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break;
                     }

                     if (var24 != null) {
                        try {
                           var24.add(var26.getString(0));
                        } catch (Throwable var19) {
                           var10000 = var19;
                           var10001 = false;
                           break;
                        }
                     }
                  }
               }

               Throwable var22 = var10000;
               var26.close();
               throw var22;
            }

            var26.close();
         }
      }
   }

   public void delete(String var1) {
      SupportSQLiteStatement var2;
      label100: {
         Throwable var10000;
         label99: {
            var2 = this.__preparedStmtOfDelete.acquire();
            this.__db.beginTransaction();
            boolean var10001;
            if (var1 == null) {
               try {
                  var2.bindNull(1);
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label99;
               }
            } else {
               try {
                  var2.bindString(1, var1);
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label99;
               }
            }

            label93:
            try {
               var2.executeUpdateDelete();
               this.__db.setTransactionSuccessful();
               break label100;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label93;
            }
         }

         Throwable var15 = var10000;
         this.__db.endTransaction();
         this.__preparedStmtOfDelete.release(var2);
         throw var15;
      }

      this.__db.endTransaction();
      this.__preparedStmtOfDelete.release(var2);
   }

   public List getAllUnfinishedWork() {
      RoomSQLiteQuery var1 = RoomSQLiteQuery.acquire("SELECT id FROM workspec WHERE state NOT IN (2, 3, 5)", 0);
      Cursor var2 = this.__db.query(var1);

      ArrayList var3;
      label78: {
         Throwable var10000;
         label77: {
            boolean var10001;
            try {
               var3 = new ArrayList(var2.getCount());
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label77;
            }

            while(true) {
               try {
                  if (!var2.moveToNext()) {
                     break label78;
                  }

                  var3.add(var2.getString(0));
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var10 = var10000;
         var2.close();
         var1.release();
         throw var10;
      }

      var2.close();
      var1.release();
      return var3;
   }

   public List getEligibleWorkForScheduling(int param1) {
      // $FF: Couldn't be decompiled
   }

   public List getInputsFromPrerequisites(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT output FROM workspec WHERE id IN (SELECT prerequisite_id FROM dependency WHERE work_spec_id=?)", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      Cursor var10 = this.__db.query(var2);

      ArrayList var3;
      label93: {
         Throwable var10000;
         label92: {
            boolean var10001;
            try {
               var3 = new ArrayList(var10.getCount());
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label92;
            }

            while(true) {
               try {
                  if (!var10.moveToNext()) {
                     break label93;
                  }

                  var3.add(Data.fromByteArray(var10.getBlob(0)));
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var11 = var10000;
         var10.close();
         var2.release();
         throw var11;
      }

      var10.close();
      var2.release();
      return var3;
   }

   public List getScheduledWork() {
      // $FF: Couldn't be decompiled
   }

   public WorkInfo.State getState(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT state FROM workspec WHERE id=?", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      Cursor var3 = this.__db.query(var2);
      boolean var5 = false;

      WorkInfo.State var7;
      label47: {
         try {
            var5 = true;
            if (var3.moveToFirst()) {
               var7 = WorkTypeConverters.intToState(var3.getInt(0));
               var5 = false;
               break label47;
            }

            var5 = false;
         } finally {
            if (var5) {
               var3.close();
               var2.release();
            }
         }

         var7 = null;
      }

      var3.close();
      var2.release();
      return var7;
   }

   public List getUnfinishedWorkWithName(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT id FROM workspec WHERE state NOT IN (2, 3, 5) AND id IN (SELECT work_spec_id FROM workname WHERE name=?)", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      Cursor var10 = this.__db.query(var2);

      ArrayList var3;
      label93: {
         Throwable var10000;
         label92: {
            boolean var10001;
            try {
               var3 = new ArrayList(var10.getCount());
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label92;
            }

            while(true) {
               try {
                  if (!var10.moveToNext()) {
                     break label93;
                  }

                  var3.add(var10.getString(0));
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var11 = var10000;
         var10.close();
         var2.release();
         throw var11;
      }

      var10.close();
      var2.release();
      return var3;
   }

   public List getUnfinishedWorkWithTag(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT id FROM workspec WHERE state NOT IN (2, 3, 5) AND id IN (SELECT work_spec_id FROM worktag WHERE tag=?)", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      Cursor var10 = this.__db.query(var2);

      ArrayList var3;
      label93: {
         Throwable var10000;
         label92: {
            boolean var10001;
            try {
               var3 = new ArrayList(var10.getCount());
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label92;
            }

            while(true) {
               try {
                  if (!var10.moveToNext()) {
                     break label93;
                  }

                  var3.add(var10.getString(0));
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var11 = var10000;
         var10.close();
         var2.release();
         throw var11;
      }

      var10.close();
      var2.release();
      return var3;
   }

   public WorkSpec getWorkSpec(String param1) {
      // $FF: Couldn't be decompiled
   }

   public List getWorkSpecIdAndStatesForName(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT id, state FROM workspec WHERE id IN (SELECT work_spec_id FROM workname WHERE name=?)", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      Cursor var13 = this.__db.query(var2);

      ArrayList var5;
      label93: {
         Throwable var10000;
         label92: {
            int var3;
            int var4;
            boolean var10001;
            try {
               var3 = var13.getColumnIndexOrThrow("id");
               var4 = var13.getColumnIndexOrThrow("state");
               var5 = new ArrayList(var13.getCount());
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label92;
            }

            while(true) {
               try {
                  if (!var13.moveToNext()) {
                     break label93;
                  }

                  WorkSpec.IdAndState var14 = new WorkSpec.IdAndState();
                  var14.id = var13.getString(var3);
                  var14.state = WorkTypeConverters.intToState(var13.getInt(var4));
                  var5.add(var14);
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var6 = var10000;
         var13.close();
         var2.release();
         throw var6;
      }

      var13.close();
      var2.release();
      return var5;
   }

   public List getWorkStatusPojoForTag(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT id, state, output FROM workspec WHERE id IN (SELECT work_spec_id FROM worktag WHERE tag=?)", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      this.__db.beginTransaction();

      ArrayList var8;
      label801: {
         Throwable var10000;
         Throwable var103;
         label805: {
            Cursor var3;
            boolean var10001;
            try {
               var3 = this.__db.query(var2);
            } catch (Throwable var101) {
               var10000 = var101;
               var10001 = false;
               break label805;
            }

            label806: {
               label807: {
                  ArrayMap var4;
                  int var5;
                  int var6;
                  int var7;
                  try {
                     var4 = new ArrayMap();
                     var5 = var3.getColumnIndexOrThrow("id");
                     var6 = var3.getColumnIndexOrThrow("state");
                     var7 = var3.getColumnIndexOrThrow("output");
                     var8 = new ArrayList(var3.getCount());
                  } catch (Throwable var99) {
                     var10000 = var99;
                     var10001 = false;
                     break label807;
                  }

                  while(true) {
                     WorkSpec.WorkInfoPojo var9;
                     label809: {
                        String var10;
                        ArrayList var11;
                        try {
                           if (!var3.moveToNext()) {
                              break;
                           }

                           var9 = new WorkSpec.WorkInfoPojo();
                           var9.id = var3.getString(var5);
                           var9.state = WorkTypeConverters.intToState(var3.getInt(var6));
                           var9.output = Data.fromByteArray(var3.getBlob(var7));
                           if (var3.isNull(var5)) {
                              break label809;
                           }

                           var10 = var3.getString(var5);
                           var11 = (ArrayList)var4.get(var10);
                        } catch (Throwable var100) {
                           var10000 = var100;
                           var10001 = false;
                           break label807;
                        }

                        ArrayList var102 = var11;
                        if (var11 == null) {
                           try {
                              var102 = new ArrayList();
                              var4.put(var10, var102);
                           } catch (Throwable var98) {
                              var10000 = var98;
                              var10001 = false;
                              break label807;
                           }
                        }

                        try {
                           var9.tags = var102;
                        } catch (Throwable var97) {
                           var10000 = var97;
                           var10001 = false;
                           break label807;
                        }
                     }

                     try {
                        var8.add(var9);
                     } catch (Throwable var96) {
                        var10000 = var96;
                        var10001 = false;
                        break label807;
                     }
                  }

                  label774:
                  try {
                     this.__fetchRelationshipWorkTagAsjavaLangString(var4);
                     this.__db.setTransactionSuccessful();
                     break label806;
                  } catch (Throwable var95) {
                     var10000 = var95;
                     var10001 = false;
                     break label774;
                  }
               }

               var103 = var10000;

               try {
                  var3.close();
                  var2.release();
                  throw var103;
               } catch (Throwable var93) {
                  var10000 = var93;
                  var10001 = false;
                  break label805;
               }
            }

            label770:
            try {
               var3.close();
               var2.release();
               break label801;
            } catch (Throwable var94) {
               var10000 = var94;
               var10001 = false;
               break label770;
            }
         }

         var103 = var10000;
         this.__db.endTransaction();
         throw var103;
      }

      this.__db.endTransaction();
      return var8;
   }

   public int incrementWorkSpecRunAttemptCount(String var1) {
      SupportSQLiteStatement var2;
      int var3;
      label100: {
         Throwable var10000;
         label99: {
            var2 = this.__preparedStmtOfIncrementWorkSpecRunAttemptCount.acquire();
            this.__db.beginTransaction();
            boolean var10001;
            if (var1 == null) {
               try {
                  var2.bindNull(1);
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label99;
               }
            } else {
               try {
                  var2.bindString(1, var1);
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label99;
               }
            }

            label93:
            try {
               var3 = var2.executeUpdateDelete();
               this.__db.setTransactionSuccessful();
               break label100;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label93;
            }
         }

         Throwable var16 = var10000;
         this.__db.endTransaction();
         this.__preparedStmtOfIncrementWorkSpecRunAttemptCount.release(var2);
         throw var16;
      }

      this.__db.endTransaction();
      this.__preparedStmtOfIncrementWorkSpecRunAttemptCount.release(var2);
      return var3;
   }

   public void insertWorkSpec(WorkSpec var1) {
      this.__db.beginTransaction();

      try {
         this.__insertionAdapterOfWorkSpec.insert((Object)var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }

   public int markWorkSpecScheduled(String var1, long var2) {
      SupportSQLiteStatement var4 = this.__preparedStmtOfMarkWorkSpecScheduled.acquire();
      this.__db.beginTransaction();

      int var5;
      label166: {
         Throwable var10000;
         label170: {
            boolean var10001;
            try {
               var4.bindLong(1, var2);
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label170;
            }

            if (var1 == null) {
               try {
                  var4.bindNull(2);
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label170;
               }
            } else {
               try {
                  var4.bindString(2, var1);
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label170;
               }
            }

            label156:
            try {
               var5 = var4.executeUpdateDelete();
               this.__db.setTransactionSuccessful();
               break label166;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label156;
            }
         }

         Throwable var26 = var10000;
         this.__db.endTransaction();
         this.__preparedStmtOfMarkWorkSpecScheduled.release(var4);
         throw var26;
      }

      this.__db.endTransaction();
      this.__preparedStmtOfMarkWorkSpecScheduled.release(var4);
      return var5;
   }

   public int resetScheduledState() {
      SupportSQLiteStatement var1 = this.__preparedStmtOfResetScheduledState.acquire();
      this.__db.beginTransaction();

      int var2;
      try {
         var2 = var1.executeUpdateDelete();
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
         this.__preparedStmtOfResetScheduledState.release(var1);
      }

      return var2;
   }

   public int resetWorkSpecRunAttemptCount(String var1) {
      SupportSQLiteStatement var2;
      int var3;
      label100: {
         Throwable var10000;
         label99: {
            var2 = this.__preparedStmtOfResetWorkSpecRunAttemptCount.acquire();
            this.__db.beginTransaction();
            boolean var10001;
            if (var1 == null) {
               try {
                  var2.bindNull(1);
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label99;
               }
            } else {
               try {
                  var2.bindString(1, var1);
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label99;
               }
            }

            label93:
            try {
               var3 = var2.executeUpdateDelete();
               this.__db.setTransactionSuccessful();
               break label100;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label93;
            }
         }

         Throwable var16 = var10000;
         this.__db.endTransaction();
         this.__preparedStmtOfResetWorkSpecRunAttemptCount.release(var2);
         throw var16;
      }

      this.__db.endTransaction();
      this.__preparedStmtOfResetWorkSpecRunAttemptCount.release(var2);
      return var3;
   }

   public void setOutput(String var1, Data var2) {
      SupportSQLiteStatement var3 = this.__preparedStmtOfSetOutput.acquire();
      this.__db.beginTransaction();

      label305: {
         Throwable var10000;
         label309: {
            byte[] var47;
            boolean var10001;
            try {
               var47 = Data.toByteArray(var2);
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label309;
            }

            if (var47 == null) {
               try {
                  var3.bindNull(1);
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label309;
               }
            } else {
               try {
                  var3.bindBlob(1, var47);
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label309;
               }
            }

            if (var1 == null) {
               try {
                  var3.bindNull(2);
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label309;
               }
            } else {
               try {
                  var3.bindString(2, var1);
               } catch (Throwable var41) {
                  var10000 = var41;
                  var10001 = false;
                  break label309;
               }
            }

            label290:
            try {
               var3.executeUpdateDelete();
               this.__db.setTransactionSuccessful();
               break label305;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               break label290;
            }
         }

         Throwable var46 = var10000;
         this.__db.endTransaction();
         this.__preparedStmtOfSetOutput.release(var3);
         throw var46;
      }

      this.__db.endTransaction();
      this.__preparedStmtOfSetOutput.release(var3);
   }

   public void setPeriodStartTime(String var1, long var2) {
      SupportSQLiteStatement var4 = this.__preparedStmtOfSetPeriodStartTime.acquire();
      this.__db.beginTransaction();

      label166: {
         Throwable var10000;
         label170: {
            boolean var10001;
            try {
               var4.bindLong(1, var2);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label170;
            }

            if (var1 == null) {
               try {
                  var4.bindNull(2);
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label170;
               }
            } else {
               try {
                  var4.bindString(2, var1);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label170;
               }
            }

            label156:
            try {
               var4.executeUpdateDelete();
               this.__db.setTransactionSuccessful();
               break label166;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label156;
            }
         }

         Throwable var25 = var10000;
         this.__db.endTransaction();
         this.__preparedStmtOfSetPeriodStartTime.release(var4);
         throw var25;
      }

      this.__db.endTransaction();
      this.__preparedStmtOfSetPeriodStartTime.release(var4);
   }

   public int setState(WorkInfo.State var1, String... var2) {
      StringBuilder var3 = StringUtil.newStringBuilder();
      var3.append("UPDATE workspec SET state=");
      var3.append("?");
      var3.append(" WHERE id IN (");
      StringUtil.appendPlaceholders(var3, var2.length);
      var3.append(")");
      String var10 = var3.toString();
      SupportSQLiteStatement var11 = this.__db.compileStatement(var10);
      var11.bindLong(1, (long)WorkTypeConverters.stateToInt(var1));
      int var4 = var2.length;
      int var5 = 2;

      int var6;
      for(var6 = 0; var6 < var4; ++var6) {
         String var9 = var2[var6];
         if (var9 == null) {
            var11.bindNull(var5);
         } else {
            var11.bindString(var5, var9);
         }

         ++var5;
      }

      this.__db.beginTransaction();

      try {
         var6 = var11.executeUpdateDelete();
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

      return var6;
   }
}
