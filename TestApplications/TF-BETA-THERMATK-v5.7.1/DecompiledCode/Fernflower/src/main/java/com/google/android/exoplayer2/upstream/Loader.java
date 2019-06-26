package com.google.android.exoplayer2.upstream;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public final class Loader implements LoaderErrorThrower {
   public static final Loader.LoadErrorAction DONT_RETRY = new Loader.LoadErrorAction(2, -9223372036854775807L);
   public static final Loader.LoadErrorAction DONT_RETRY_FATAL = new Loader.LoadErrorAction(3, -9223372036854775807L);
   public static final Loader.LoadErrorAction RETRY = createRetryAction(false, -9223372036854775807L);
   public static final Loader.LoadErrorAction RETRY_RESET_ERROR_COUNT = createRetryAction(true, -9223372036854775807L);
   private Loader.LoadTask currentTask;
   private final ExecutorService downloadExecutorService;
   private IOException fatalError;

   public Loader(String var1) {
      this.downloadExecutorService = Util.newSingleThreadExecutor(var1);
   }

   public static Loader.LoadErrorAction createRetryAction(boolean var0, long var1) {
      return new Loader.LoadErrorAction(var0, var1);
   }

   public void cancelLoading() {
      this.currentTask.cancel(false);
   }

   public boolean isLoading() {
      boolean var1;
      if (this.currentTask != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void maybeThrowError() throws IOException {
      this.maybeThrowError(Integer.MIN_VALUE);
   }

   public void maybeThrowError(int var1) throws IOException {
      IOException var2 = this.fatalError;
      if (var2 == null) {
         Loader.LoadTask var4 = this.currentTask;
         if (var4 != null) {
            int var3 = var1;
            if (var1 == Integer.MIN_VALUE) {
               var3 = var4.defaultMinRetryCount;
            }

            var4.maybeThrowError(var3);
         }

      } else {
         throw var2;
      }
   }

   public void release() {
      this.release((Loader.ReleaseCallback)null);
   }

   public void release(Loader.ReleaseCallback var1) {
      Loader.LoadTask var2 = this.currentTask;
      if (var2 != null) {
         var2.cancel(true);
      }

      if (var1 != null) {
         this.downloadExecutorService.execute(new Loader.ReleaseTask(var1));
      }

      this.downloadExecutorService.shutdown();
   }

   public long startLoading(Loader.Loadable var1, Loader.Callback var2, int var3) {
      Looper var4 = Looper.myLooper();
      boolean var5;
      if (var4 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Assertions.checkState(var5);
      this.fatalError = null;
      long var6 = SystemClock.elapsedRealtime();
      (new Loader.LoadTask(var4, var1, var2, var3, var6)).start(0L);
      return var6;
   }

   public interface Callback {
      void onLoadCanceled(Loader.Loadable var1, long var2, long var4, boolean var6);

      void onLoadCompleted(Loader.Loadable var1, long var2, long var4);

      Loader.LoadErrorAction onLoadError(Loader.Loadable var1, long var2, long var4, IOException var6, int var7);
   }

   public static final class LoadErrorAction {
      private final long retryDelayMillis;
      private final int type;

      private LoadErrorAction(int var1, long var2) {
         this.type = var1;
         this.retryDelayMillis = var2;
      }

      // $FF: synthetic method
      LoadErrorAction(int var1, long var2, Object var4) {
         this(var1, var2);
      }

      public boolean isRetry() {
         int var1 = this.type;
         boolean var2 = true;
         boolean var3 = var2;
         if (var1 != 0) {
            if (var1 == 1) {
               var3 = var2;
            } else {
               var3 = false;
            }
         }

         return var3;
      }
   }

   @SuppressLint({"HandlerLeak"})
   private final class LoadTask extends Handler implements Runnable {
      private Loader.Callback callback;
      private volatile boolean canceled;
      private IOException currentError;
      public final int defaultMinRetryCount;
      private int errorCount;
      private volatile Thread executorThread;
      private final Loader.Loadable loadable;
      private volatile boolean released;
      private final long startTimeMs;

      public LoadTask(Looper var2, Loader.Loadable var3, Loader.Callback var4, int var5, long var6) {
         super(var2);
         this.loadable = var3;
         this.callback = var4;
         this.defaultMinRetryCount = var5;
         this.startTimeMs = var6;
      }

      private void execute() {
         this.currentError = null;
         Loader.this.downloadExecutorService.execute(Loader.this.currentTask);
      }

      private void finish() {
         Loader.this.currentTask = null;
      }

      private long getRetryDelayMillis() {
         return (long)Math.min((this.errorCount - 1) * 1000, 5000);
      }

      public void cancel(boolean var1) {
         this.released = var1;
         this.currentError = null;
         if (this.hasMessages(0)) {
            this.removeMessages(0);
            if (!var1) {
               this.sendEmptyMessage(1);
            }
         } else {
            this.canceled = true;
            this.loadable.cancelLoad();
            if (this.executorThread != null) {
               this.executorThread.interrupt();
            }
         }

         if (var1) {
            this.finish();
            long var2 = SystemClock.elapsedRealtime();
            this.callback.onLoadCanceled(this.loadable, var2, var2 - this.startTimeMs, true);
            this.callback = null;
         }

      }

      public void handleMessage(Message var1) {
         if (!this.released) {
            int var2 = var1.what;
            if (var2 == 0) {
               this.execute();
            } else if (var2 != 4) {
               this.finish();
               long var3 = SystemClock.elapsedRealtime();
               long var5 = var3 - this.startTimeMs;
               if (this.canceled) {
                  this.callback.onLoadCanceled(this.loadable, var3, var5, false);
               } else {
                  var2 = var1.what;
                  if (var2 != 1) {
                     if (var2 != 2) {
                        if (var2 == 3) {
                           this.currentError = (IOException)var1.obj;
                           ++this.errorCount;
                           Loader.LoadErrorAction var8 = this.callback.onLoadError(this.loadable, var3, var5, this.currentError, this.errorCount);
                           if (var8.type == 3) {
                              Loader.this.fatalError = this.currentError;
                           } else if (var8.type != 2) {
                              if (var8.type == 1) {
                                 this.errorCount = 1;
                              }

                              if (var8.retryDelayMillis != -9223372036854775807L) {
                                 var5 = var8.retryDelayMillis;
                              } else {
                                 var5 = this.getRetryDelayMillis();
                              }

                              this.start(var5);
                           }
                        }
                     } else {
                        try {
                           this.callback.onLoadCompleted(this.loadable, var3, var5);
                        } catch (RuntimeException var7) {
                           Log.e("LoadTask", "Unexpected exception handling load completed", var7);
                           Loader.this.fatalError = new Loader.UnexpectedLoaderException(var7);
                        }
                     }
                  } else {
                     this.callback.onLoadCanceled(this.loadable, var3, var5, false);
                  }

               }
            } else {
               throw (Error)var1.obj;
            }
         }
      }

      public void maybeThrowError(int var1) throws IOException {
         IOException var2 = this.currentError;
         if (var2 != null && this.errorCount > var1) {
            throw var2;
         }
      }

      public void run() {
         Error var10000;
         label188: {
            label189: {
               Exception var44;
               label190: {
                  OutOfMemoryError var43;
                  label191: {
                     IOException var45;
                     label165: {
                        boolean var10001;
                        label164: {
                           try {
                              this.executorThread = Thread.currentThread();
                              if (this.canceled) {
                                 break label164;
                              }

                              StringBuilder var1 = new StringBuilder();
                              var1.append("load:");
                              var1.append(this.loadable.getClass().getSimpleName());
                              TraceUtil.beginSection(var1.toString());
                           } catch (IOException var34) {
                              var45 = var34;
                              var10001 = false;
                              break label165;
                           } catch (InterruptedException var35) {
                              var10001 = false;
                              break label189;
                           } catch (Exception var36) {
                              var44 = var36;
                              var10001 = false;
                              break label190;
                           } catch (OutOfMemoryError var37) {
                              var43 = var37;
                              var10001 = false;
                              break label191;
                           } catch (Error var38) {
                              var10000 = var38;
                              var10001 = false;
                              break label188;
                           }

                           try {
                              this.loadable.load();
                           } finally {
                              try {
                                 TraceUtil.endSection();
                              } catch (IOException var23) {
                                 var45 = var23;
                                 var10001 = false;
                                 break label165;
                              } catch (InterruptedException var24) {
                                 var10001 = false;
                                 break label189;
                              } catch (Exception var25) {
                                 var44 = var25;
                                 var10001 = false;
                                 break label190;
                              } catch (OutOfMemoryError var26) {
                                 var43 = var26;
                                 var10001 = false;
                                 break label191;
                              } catch (Error var27) {
                                 var10000 = var27;
                                 var10001 = false;
                                 break label188;
                              }
                           }
                        }

                        try {
                           if (!this.released) {
                              this.sendEmptyMessage(2);
                           }

                           return;
                        } catch (IOException var28) {
                           var45 = var28;
                           var10001 = false;
                        } catch (InterruptedException var29) {
                           var10001 = false;
                           break label189;
                        } catch (Exception var30) {
                           var44 = var30;
                           var10001 = false;
                           break label190;
                        } catch (OutOfMemoryError var31) {
                           var43 = var31;
                           var10001 = false;
                           break label191;
                        } catch (Error var32) {
                           var10000 = var32;
                           var10001 = false;
                           break label188;
                        }
                     }

                     IOException var42 = var45;
                     if (!this.released) {
                        this.obtainMessage(3, var42).sendToTarget();
                     }

                     return;
                  }

                  OutOfMemoryError var40 = var43;
                  Log.e("LoadTask", "OutOfMemory error loading stream", var40);
                  if (!this.released) {
                     this.obtainMessage(3, new Loader.UnexpectedLoaderException(var40)).sendToTarget();
                  }

                  return;
               }

               Exception var41 = var44;
               Log.e("LoadTask", "Unexpected exception loading stream", var41);
               if (!this.released) {
                  this.obtainMessage(3, new Loader.UnexpectedLoaderException(var41)).sendToTarget();
               }

               return;
            }

            Assertions.checkState(this.canceled);
            if (!this.released) {
               this.sendEmptyMessage(2);
            }

            return;
         }

         Error var39 = var10000;
         Log.e("LoadTask", "Unexpected error loading stream", var39);
         if (!this.released) {
            this.obtainMessage(4, var39).sendToTarget();
         }

         throw var39;
      }

      public void start(long var1) {
         boolean var3;
         if (Loader.this.currentTask == null) {
            var3 = true;
         } else {
            var3 = false;
         }

         Assertions.checkState(var3);
         Loader.this.currentTask = this;
         if (var1 > 0L) {
            this.sendEmptyMessageDelayed(0, var1);
         } else {
            this.execute();
         }

      }
   }

   public interface Loadable {
      void cancelLoad();

      void load() throws IOException, InterruptedException;
   }

   public interface ReleaseCallback {
      void onLoaderReleased();
   }

   private static final class ReleaseTask implements Runnable {
      private final Loader.ReleaseCallback callback;

      public ReleaseTask(Loader.ReleaseCallback var1) {
         this.callback = var1;
      }

      public void run() {
         this.callback.onLoaderReleased();
      }
   }

   public static final class UnexpectedLoaderException extends IOException {
      public UnexpectedLoaderException(Throwable var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unexpected ");
         var2.append(var1.getClass().getSimpleName());
         var2.append(": ");
         var2.append(var1.getMessage());
         super(var2.toString(), var1);
      }
   }
}
