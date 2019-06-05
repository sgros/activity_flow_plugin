package org.greenrobot.greendao.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

class AsyncOperationExecutor implements Runnable, Callback {
   private static ExecutorService executorService = Executors.newCachedThreadPool();
   private int countOperationsCompleted;
   private int countOperationsEnqueued;
   private volatile boolean executorRunning;
   private Handler handlerMainThread;
   private int lastSequenceNumber;
   private volatile AsyncOperationListener listener;
   private volatile AsyncOperationListener listenerMainThread;
   private volatile int maxOperationCountToMerge = 50;
   private final BlockingQueue queue = new LinkedBlockingQueue();
   private volatile int waitForMergeMillis = 50;

   private void executeOperation(AsyncOperation var1) {
      var1.timeStarted = System.currentTimeMillis();

      label253: {
         Throwable var10000;
         label252: {
            boolean var10001;
            label251: {
               label250: {
                  label249: {
                     label248: {
                        label247: {
                           label246: {
                              label245: {
                                 label244: {
                                    label257: {
                                       label242: {
                                          label241: {
                                             label240: {
                                                label239: {
                                                   label238: {
                                                      label237: {
                                                         label236: {
                                                            label235: {
                                                               label234: {
                                                                  label233: {
                                                                     label232: {
                                                                        label231: {
                                                                           label230: {
                                                                              try {
                                                                                 switch(var1.type) {
                                                                                 case Delete:
                                                                                    break label246;
                                                                                 case DeleteInTxIterable:
                                                                                    break label234;
                                                                                 case DeleteInTxArray:
                                                                                    break label237;
                                                                                 case Insert:
                                                                                    break label250;
                                                                                 case InsertInTxIterable:
                                                                                    break label241;
                                                                                 case InsertInTxArray:
                                                                                    break label230;
                                                                                 case InsertOrReplace:
                                                                                    break label245;
                                                                                 case InsertOrReplaceInTxIterable:
                                                                                    break;
                                                                                 case InsertOrReplaceInTxArray:
                                                                                    break label248;
                                                                                 case Update:
                                                                                    break label232;
                                                                                 case UpdateInTxIterable:
                                                                                    break label238;
                                                                                 case UpdateInTxArray:
                                                                                    break label249;
                                                                                 case TransactionRunnable:
                                                                                    break label231;
                                                                                 case TransactionCallable:
                                                                                    break label236;
                                                                                 case QueryList:
                                                                                    break label257;
                                                                                 case QueryUnique:
                                                                                    break label244;
                                                                                 case DeleteByKey:
                                                                                    break label251;
                                                                                 case DeleteAll:
                                                                                    break label239;
                                                                                 case Load:
                                                                                    break label233;
                                                                                 case LoadAll:
                                                                                    break label240;
                                                                                 case Count:
                                                                                    break label235;
                                                                                 case Refresh:
                                                                                    break label247;
                                                                                 default:
                                                                                    break label242;
                                                                                 }
                                                                              } catch (Throwable var28) {
                                                                                 var10000 = var28;
                                                                                 var10001 = false;
                                                                                 break label252;
                                                                              }

                                                                              try {
                                                                                 var1.dao.insertOrReplaceInTx((Iterable)var1.parameter);
                                                                                 break label253;
                                                                              } catch (Throwable var12) {
                                                                                 var10000 = var12;
                                                                                 var10001 = false;
                                                                                 break label252;
                                                                              }
                                                                           }

                                                                           try {
                                                                              var1.dao.insertInTx((Object[])var1.parameter);
                                                                              break label253;
                                                                           } catch (Throwable var11) {
                                                                              var10000 = var11;
                                                                              var10001 = false;
                                                                              break label252;
                                                                           }
                                                                        }

                                                                        try {
                                                                           this.executeTransactionRunnable(var1);
                                                                           break label253;
                                                                        } catch (Throwable var27) {
                                                                           var10000 = var27;
                                                                           var10001 = false;
                                                                           break label252;
                                                                        }
                                                                     }

                                                                     try {
                                                                        var1.dao.update(var1.parameter);
                                                                        break label253;
                                                                     } catch (Throwable var10) {
                                                                        var10000 = var10;
                                                                        var10001 = false;
                                                                        break label252;
                                                                     }
                                                                  }

                                                                  try {
                                                                     var1.result = var1.dao.load(var1.parameter);
                                                                     break label253;
                                                                  } catch (Throwable var26) {
                                                                     var10000 = var26;
                                                                     var10001 = false;
                                                                     break label252;
                                                                  }
                                                               }

                                                               try {
                                                                  var1.dao.deleteInTx((Iterable)var1.parameter);
                                                                  break label253;
                                                               } catch (Throwable var9) {
                                                                  var10000 = var9;
                                                                  var10001 = false;
                                                                  break label252;
                                                               }
                                                            }

                                                            try {
                                                               var1.result = var1.dao.count();
                                                               break label253;
                                                            } catch (Throwable var25) {
                                                               var10000 = var25;
                                                               var10001 = false;
                                                               break label252;
                                                            }
                                                         }

                                                         try {
                                                            this.executeTransactionCallable(var1);
                                                            break label253;
                                                         } catch (Throwable var8) {
                                                            var10000 = var8;
                                                            var10001 = false;
                                                            break label252;
                                                         }
                                                      }

                                                      try {
                                                         var1.dao.deleteInTx((Object[])var1.parameter);
                                                         break label253;
                                                      } catch (Throwable var24) {
                                                         var10000 = var24;
                                                         var10001 = false;
                                                         break label252;
                                                      }
                                                   }

                                                   try {
                                                      var1.dao.updateInTx((Iterable)var1.parameter);
                                                      break label253;
                                                   } catch (Throwable var7) {
                                                      var10000 = var7;
                                                      var10001 = false;
                                                      break label252;
                                                   }
                                                }

                                                try {
                                                   var1.dao.deleteAll();
                                                   break label253;
                                                } catch (Throwable var23) {
                                                   var10000 = var23;
                                                   var10001 = false;
                                                   break label252;
                                                }
                                             }

                                             try {
                                                var1.result = var1.dao.loadAll();
                                                break label253;
                                             } catch (Throwable var6) {
                                                var10000 = var6;
                                                var10001 = false;
                                                break label252;
                                             }
                                          }

                                          try {
                                             var1.dao.insertInTx((Iterable)var1.parameter);
                                             break label253;
                                          } catch (Throwable var22) {
                                             var10000 = var22;
                                             var10001 = false;
                                             break label252;
                                          }
                                       }

                                       DaoException var2;
                                       try {
                                          var2 = new DaoException;
                                       } catch (Throwable var5) {
                                          var10000 = var5;
                                          var10001 = false;
                                          break label252;
                                       }

                                       try {
                                          StringBuilder var29 = new StringBuilder();
                                          var29.append("Unsupported operation: ");
                                          var29.append(var1.type);
                                          var2.<init>(var29.toString());
                                          throw var2;
                                       } catch (Throwable var4) {
                                          var10000 = var4;
                                          var10001 = false;
                                          break label252;
                                       }
                                    }

                                    try {
                                       var1.result = ((Query)var1.parameter).forCurrentThread().list();
                                       break label253;
                                    } catch (Throwable var21) {
                                       var10000 = var21;
                                       var10001 = false;
                                       break label252;
                                    }
                                 }

                                 try {
                                    var1.result = ((Query)var1.parameter).forCurrentThread().unique();
                                    break label253;
                                 } catch (Throwable var20) {
                                    var10000 = var20;
                                    var10001 = false;
                                    break label252;
                                 }
                              }

                              try {
                                 var1.dao.insertOrReplace(var1.parameter);
                                 break label253;
                              } catch (Throwable var19) {
                                 var10000 = var19;
                                 var10001 = false;
                                 break label252;
                              }
                           }

                           try {
                              var1.dao.delete(var1.parameter);
                              break label253;
                           } catch (Throwable var18) {
                              var10000 = var18;
                              var10001 = false;
                              break label252;
                           }
                        }

                        try {
                           var1.dao.refresh(var1.parameter);
                           break label253;
                        } catch (Throwable var17) {
                           var10000 = var17;
                           var10001 = false;
                           break label252;
                        }
                     }

                     try {
                        var1.dao.insertOrReplaceInTx((Object[])var1.parameter);
                        break label253;
                     } catch (Throwable var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label252;
                     }
                  }

                  try {
                     var1.dao.updateInTx((Object[])var1.parameter);
                     break label253;
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label252;
                  }
               }

               try {
                  var1.dao.insert(var1.parameter);
                  break label253;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label252;
               }
            }

            try {
               var1.dao.deleteByKey(var1.parameter);
               break label253;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
            }
         }

         Throwable var3 = var10000;
         var1.throwable = var3;
      }

      var1.timeCompleted = System.currentTimeMillis();
   }

   private void executeOperationAndPostCompleted(AsyncOperation var1) {
      this.executeOperation(var1);
      this.handleOperationCompleted(var1);
   }

   private void executeTransactionCallable(AsyncOperation var1) throws Exception {
      Database var2 = var1.getDatabase();
      var2.beginTransaction();

      try {
         var1.result = ((Callable)var1.parameter).call();
         var2.setTransactionSuccessful();
      } finally {
         var2.endTransaction();
      }

   }

   private void executeTransactionRunnable(AsyncOperation var1) {
      Database var2 = var1.getDatabase();
      var2.beginTransaction();

      try {
         ((Runnable)var1.parameter).run();
         var2.setTransactionSuccessful();
      } finally {
         var2.endTransaction();
      }

   }

   private void handleOperationCompleted(AsyncOperation var1) {
      var1.setCompleted();
      AsyncOperationListener var2 = this.listener;
      if (var2 != null) {
         var2.onAsyncOperationCompleted(var1);
      }

      if (this.listenerMainThread != null) {
         if (this.handlerMainThread == null) {
            this.handlerMainThread = new Handler(Looper.getMainLooper(), this);
         }

         Message var15 = this.handlerMainThread.obtainMessage(1, var1);
         this.handlerMainThread.sendMessage(var15);
      }

      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label167: {
         try {
            ++this.countOperationsCompleted;
            if (this.countOperationsCompleted == this.countOperationsEnqueued) {
               this.notifyAll();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label167;
         }

         label164:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label164;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   private void mergeTxAndExecute(AsyncOperation var1, AsyncOperation var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1);
      var3.add(var2);
      Database var65 = var1.getDatabase();
      var65.beginTransaction();
      boolean var4 = false;
      int var5 = 0;

      while(true) {
         label675: {
            boolean var7;
            label670: {
               label676: {
                  Throwable var10000;
                  label677: {
                     int var6;
                     boolean var10001;
                     try {
                        var6 = var3.size();
                     } catch (Throwable var64) {
                        var10000 = var64;
                        var10001 = false;
                        break label677;
                     }

                     var7 = true;
                     if (var5 >= var6) {
                        break label676;
                     }

                     AsyncOperation var8;
                     try {
                        var8 = (AsyncOperation)var3.get(var5);
                        this.executeOperation(var8);
                        if (var8.isFailed()) {
                           break label676;
                        }
                     } catch (Throwable var63) {
                        var10000 = var63;
                        var10001 = false;
                        break label677;
                     }

                     label657: {
                        try {
                           if (var5 != var3.size() - 1) {
                              break label675;
                           }

                           var2 = (AsyncOperation)this.queue.peek();
                           if (var5 < this.maxOperationCountToMerge && var8.isMergeableWith(var2)) {
                              var8 = (AsyncOperation)this.queue.remove();
                              break label657;
                           }
                        } catch (Throwable var62) {
                           var10000 = var62;
                           var10001 = false;
                           break label677;
                        }

                        try {
                           var65.setTransactionSuccessful();
                           break label670;
                        } catch (Throwable var61) {
                           var10000 = var61;
                           var10001 = false;
                           break label677;
                        }
                     }

                     if (var8 != var2) {
                        label640:
                        try {
                           DaoException var67 = new DaoException("Internal error: peeked op did not match removed op");
                           throw var67;
                        } catch (Throwable var59) {
                           var10000 = var59;
                           var10001 = false;
                           break label640;
                        }
                     } else {
                        label644:
                        try {
                           var3.add(var8);
                           break label675;
                        } catch (Throwable var60) {
                           var10000 = var60;
                           var10001 = false;
                           break label644;
                        }
                     }
                  }

                  Throwable var68 = var10000;

                  try {
                     var65.endTransaction();
                  } catch (RuntimeException var57) {
                     StringBuilder var69 = new StringBuilder();
                     var69.append("Async transaction could not be ended, success so far was: ");
                     var69.append(false);
                     DaoLog.i(var69.toString(), var57);
                  }

                  throw var68;
               }

               var7 = false;
            }

            try {
               var65.endTransaction();
            } catch (RuntimeException var58) {
               StringBuilder var70 = new StringBuilder();
               var70.append("Async transaction could not be ended, success so far was: ");
               var70.append(var7);
               DaoLog.i(var70.toString(), var58);
               var7 = var4;
            }

            Iterator var66;
            if (var7) {
               var5 = var3.size();
               var66 = var3.iterator();

               while(var66.hasNext()) {
                  var2 = (AsyncOperation)var66.next();
                  var2.mergedOperationsCount = var5;
                  this.handleOperationCompleted(var2);
               }
            } else {
               DaoLog.i("Reverted merged transaction because one of the operations failed. Executing operations one by one instead...");
               var66 = var3.iterator();

               while(var66.hasNext()) {
                  var2 = (AsyncOperation)var66.next();
                  var2.reset();
                  this.executeOperationAndPostCompleted(var2);
               }
            }

            return;
         }

         ++var5;
      }
   }

   public void enqueue(AsyncOperation var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            int var2 = this.lastSequenceNumber + 1;
            this.lastSequenceNumber = var2;
            var1.sequenceNumber = var2;
            this.queue.add(var1);
            ++this.countOperationsEnqueued;
            if (!this.executorRunning) {
               this.executorRunning = true;
               executorService.execute(this);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public AsyncOperationListener getListener() {
      return this.listener;
   }

   public AsyncOperationListener getListenerMainThread() {
      return this.listenerMainThread;
   }

   public int getMaxOperationCountToMerge() {
      return this.maxOperationCountToMerge;
   }

   public int getWaitForMergeMillis() {
      return this.waitForMergeMillis;
   }

   public boolean handleMessage(Message var1) {
      AsyncOperationListener var2 = this.listenerMainThread;
      if (var2 != null) {
         var2.onAsyncOperationCompleted((AsyncOperation)var1.obj);
      }

      return false;
   }

   public boolean isCompleted() {
      synchronized(this){}
      boolean var6 = false;

      int var1;
      int var2;
      try {
         var6 = true;
         var1 = this.countOperationsEnqueued;
         var2 = this.countOperationsCompleted;
         var6 = false;
      } finally {
         if (var6) {
            ;
         }
      }

      boolean var3;
      if (var1 == var2) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void setListener(AsyncOperationListener var1) {
      this.listener = var1;
   }

   public void setListenerMainThread(AsyncOperationListener var1) {
      this.listenerMainThread = var1;
   }

   public void setMaxOperationCountToMerge(int var1) {
      this.maxOperationCountToMerge = var1;
   }

   public void setWaitForMergeMillis(int var1) {
      this.waitForMergeMillis = var1;
   }

   public void waitForCompletion() {
      // $FF: Couldn't be decompiled
   }

   public boolean waitForCompletion(int param1) {
      // $FF: Couldn't be decompiled
   }
}
