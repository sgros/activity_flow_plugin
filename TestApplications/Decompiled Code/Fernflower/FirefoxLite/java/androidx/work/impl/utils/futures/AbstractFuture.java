package androidx.work.impl.utils.futures;

import com.google.common.util.concurrent.ListenableFuture;
import java.util.Locale;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractFuture implements ListenableFuture {
   static final AbstractFuture.AtomicHelper ATOMIC_HELPER;
   static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
   private static final Object NULL;
   private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
   volatile AbstractFuture.Listener listeners;
   volatile Object value;
   volatile AbstractFuture.Waiter waiters;

   static {
      Object var0;
      Throwable var1;
      label17: {
         try {
            var0 = new AbstractFuture.SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.Waiter.class, AbstractFuture.Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, AbstractFuture.Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, AbstractFuture.Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
         } catch (Throwable var2) {
            var1 = var2;
            var0 = new AbstractFuture.SynchronizedHelper();
            break label17;
         }

         var1 = null;
      }

      ATOMIC_HELPER = (AbstractFuture.AtomicHelper)var0;
      if (var1 != null) {
         log.log(Level.SEVERE, "SafeAtomicHelper is broken!", var1);
      }

      NULL = new Object();
   }

   protected AbstractFuture() {
   }

   private void addDoneString(StringBuilder var1) {
      try {
         Object var2 = getUninterruptibly(this);
         var1.append("SUCCESS, result=[");
         var1.append(this.userObjectToString(var2));
         var1.append("]");
      } catch (ExecutionException var3) {
         var1.append("FAILURE, cause=[");
         var1.append(var3.getCause());
         var1.append("]");
      } catch (CancellationException var4) {
         var1.append("CANCELLED");
      } catch (RuntimeException var5) {
         var1.append("UNKNOWN, cause=[");
         var1.append(var5.getClass());
         var1.append(" thrown from get()]");
      }

   }

   private static CancellationException cancellationExceptionWithCause(String var0, Throwable var1) {
      CancellationException var2 = new CancellationException(var0);
      var2.initCause(var1);
      return var2;
   }

   static Object checkNotNull(Object var0) {
      if (var0 != null) {
         return var0;
      } else {
         throw new NullPointerException();
      }
   }

   private AbstractFuture.Listener clearListeners(AbstractFuture.Listener var1) {
      AbstractFuture.Listener var2;
      do {
         var2 = this.listeners;
      } while(!ATOMIC_HELPER.casListeners(this, var2, AbstractFuture.Listener.TOMBSTONE));

      AbstractFuture.Listener var3 = var1;

      for(var1 = var2; var1 != null; var1 = var2) {
         var2 = var1.next;
         var1.next = var3;
         var3 = var1;
      }

      return var3;
   }

   static void complete(AbstractFuture var0) {
      AbstractFuture.Listener var1 = null;

      label24:
      while(true) {
         var0.releaseWaiters();
         var0.afterDone();

         AbstractFuture.Listener var4;
         for(var1 = var0.clearListeners(var1); var1 != null; var1 = var4) {
            var4 = var1.next;
            Runnable var2 = var1.task;
            if (var2 instanceof AbstractFuture.SetFuture) {
               AbstractFuture.SetFuture var5 = (AbstractFuture.SetFuture)var2;
               AbstractFuture var6 = var5.owner;
               if (var6.value == var5) {
                  Object var3 = getFutureValue(var5.future);
                  if (ATOMIC_HELPER.casValue(var6, var5, var3)) {
                     var1 = var4;
                     var0 = var6;
                     continue label24;
                  }
               }
            } else {
               executeListener(var2, var1.executor);
            }
         }

         return;
      }
   }

   private static void executeListener(Runnable var0, Executor var1) {
      try {
         var1.execute(var0);
      } catch (RuntimeException var6) {
         Logger var3 = log;
         Level var4 = Level.SEVERE;
         StringBuilder var5 = new StringBuilder();
         var5.append("RuntimeException while executing runnable ");
         var5.append(var0);
         var5.append(" with executor ");
         var5.append(var1);
         var3.log(var4, var5.toString(), var6);
      }

   }

   private Object getDoneValue(Object var1) throws ExecutionException {
      if (!(var1 instanceof AbstractFuture.Cancellation)) {
         if (!(var1 instanceof AbstractFuture.Failure)) {
            return var1 == NULL ? null : var1;
         } else {
            throw new ExecutionException(((AbstractFuture.Failure)var1).exception);
         }
      } else {
         throw cancellationExceptionWithCause("Task was cancelled.", ((AbstractFuture.Cancellation)var1).cause);
      }
   }

   static Object getFutureValue(ListenableFuture var0) {
      Object var1;
      if (var0 instanceof AbstractFuture) {
         var1 = ((AbstractFuture)var0).value;
         Object var12 = var1;
         if (var1 instanceof AbstractFuture.Cancellation) {
            AbstractFuture.Cancellation var15 = (AbstractFuture.Cancellation)var1;
            var12 = var1;
            if (var15.wasInterrupted) {
               if (var15.cause != null) {
                  var12 = new AbstractFuture.Cancellation(false, var15.cause);
               } else {
                  var12 = AbstractFuture.Cancellation.CAUSELESS_CANCELLED;
               }
            }
         }

         return var12;
      } else {
         boolean var3 = var0.isCancelled();
         if ((GENERATE_CANCELLATION_CAUSES ^ true) & var3) {
            return AbstractFuture.Cancellation.CAUSELESS_CANCELLED;
         } else {
            CancellationException var16;
            label53: {
               ExecutionException var17;
               label52: {
                  Throwable var10000;
                  label61: {
                     Object var2;
                     boolean var10001;
                     try {
                        var2 = getUninterruptibly(var0);
                     } catch (ExecutionException var7) {
                        var17 = var7;
                        var10001 = false;
                        break label52;
                     } catch (CancellationException var8) {
                        var16 = var8;
                        var10001 = false;
                        break label53;
                     } catch (Throwable var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label61;
                     }

                     var1 = var2;
                     if (var2 != null) {
                        return var1;
                     }

                     try {
                        var1 = NULL;
                        return var1;
                     } catch (ExecutionException var4) {
                        var17 = var4;
                        var10001 = false;
                        break label52;
                     } catch (CancellationException var5) {
                        var16 = var5;
                        var10001 = false;
                        break label53;
                     } catch (Throwable var6) {
                        var10000 = var6;
                        var10001 = false;
                     }
                  }

                  Throwable var10 = var10000;
                  return new AbstractFuture.Failure(var10);
               }

               ExecutionException var11 = var17;
               return new AbstractFuture.Failure(var11.getCause());
            }

            CancellationException var14 = var16;
            if (!var3) {
               StringBuilder var13 = new StringBuilder();
               var13.append("get() threw CancellationException, despite reporting isCancelled() == false: ");
               var13.append(var0);
               return new AbstractFuture.Failure(new IllegalArgumentException(var13.toString(), var14));
            } else {
               return new AbstractFuture.Cancellation(false, var14);
            }
         }
      }
   }

   private static Object getUninterruptibly(Future var0) throws ExecutionException {
      boolean var1 = false;

      Object var2;
      while(true) {
         boolean var5 = false;

         try {
            var5 = true;
            var2 = var0.get();
            var5 = false;
            break;
         } catch (InterruptedException var6) {
            var5 = false;
         } finally {
            if (var5) {
               if (var1) {
                  Thread.currentThread().interrupt();
               }

            }
         }

         var1 = true;
      }

      if (var1) {
         Thread.currentThread().interrupt();
      }

      return var2;
   }

   private void releaseWaiters() {
      AbstractFuture.Waiter var1;
      do {
         var1 = this.waiters;
      } while(!ATOMIC_HELPER.casWaiters(this, var1, AbstractFuture.Waiter.TOMBSTONE));

      while(var1 != null) {
         var1.unpark();
         var1 = var1.next;
      }

   }

   private void removeWaiter(AbstractFuture.Waiter var1) {
      var1.thread = null;

      label30:
      while(true) {
         var1 = this.waiters;
         if (var1 == AbstractFuture.Waiter.TOMBSTONE) {
            return;
         }

         AbstractFuture.Waiter var4;
         for(AbstractFuture.Waiter var2 = null; var1 != null; var2 = var4) {
            AbstractFuture.Waiter var3 = var1.next;
            if (var1.thread != null) {
               var4 = var1;
            } else if (var2 != null) {
               var2.next = var3;
               var4 = var2;
               if (var2.thread == null) {
                  continue label30;
               }
            } else {
               var4 = var2;
               if (!ATOMIC_HELPER.casWaiters(this, var1, var3)) {
                  continue label30;
               }
            }

            var1 = var3;
         }

         return;
      }
   }

   private String userObjectToString(Object var1) {
      return var1 == this ? "this future" : String.valueOf(var1);
   }

   public final void addListener(Runnable var1, Executor var2) {
      checkNotNull(var1);
      checkNotNull(var2);
      AbstractFuture.Listener var3 = this.listeners;
      if (var3 != AbstractFuture.Listener.TOMBSTONE) {
         AbstractFuture.Listener var4 = new AbstractFuture.Listener(var1, var2);

         AbstractFuture.Listener var5;
         do {
            var4.next = var3;
            if (ATOMIC_HELPER.casListeners(this, var3, var4)) {
               return;
            }

            var5 = this.listeners;
            var3 = var5;
         } while(var5 != AbstractFuture.Listener.TOMBSTONE);
      }

      executeListener(var1, var2);
   }

   protected void afterDone() {
   }

   public final boolean cancel(boolean var1) {
      Object var2 = this.value;
      boolean var3 = true;
      boolean var4;
      if (var2 == null) {
         var4 = true;
      } else {
         var4 = false;
      }

      boolean var6;
      if (var4 | var2 instanceof AbstractFuture.SetFuture) {
         AbstractFuture.Cancellation var5;
         if (GENERATE_CANCELLATION_CAUSES) {
            var5 = new AbstractFuture.Cancellation(var1, new CancellationException("Future.cancel() was called."));
         } else if (var1) {
            var5 = AbstractFuture.Cancellation.CAUSELESS_INTERRUPTED;
         } else {
            var5 = AbstractFuture.Cancellation.CAUSELESS_CANCELLED;
         }

         var6 = false;
         AbstractFuture var7 = this;

         while(true) {
            while(!ATOMIC_HELPER.casValue(var7, var2, var5)) {
               Object var8 = var7.value;
               var2 = var8;
               if (!(var8 instanceof AbstractFuture.SetFuture)) {
                  return var6;
               }
            }

            if (var1) {
               var7.interruptTask();
            }

            complete(var7);
            var6 = var3;
            if (!(var2 instanceof AbstractFuture.SetFuture)) {
               break;
            }

            ListenableFuture var9 = ((AbstractFuture.SetFuture)var2).future;
            if (!(var9 instanceof AbstractFuture)) {
               var9.cancel(var1);
               var6 = var3;
               break;
            }

            var7 = (AbstractFuture)var9;
            var2 = var7.value;
            if (var2 == null) {
               var4 = true;
            } else {
               var4 = false;
            }

            var6 = var3;
            if (!(var4 | var2 instanceof AbstractFuture.SetFuture)) {
               break;
            }

            var6 = true;
         }
      } else {
         var6 = false;
      }

      return var6;
   }

   public final Object get() throws InterruptedException, ExecutionException {
      if (!Thread.interrupted()) {
         Object var1 = this.value;
         boolean var2;
         if (var1 != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         boolean var3;
         if (!(var1 instanceof AbstractFuture.SetFuture)) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (var2 & var3) {
            return this.getDoneValue(var1);
         } else {
            AbstractFuture.Waiter var6 = this.waiters;
            if (var6 != AbstractFuture.Waiter.TOMBSTONE) {
               AbstractFuture.Waiter var4 = new AbstractFuture.Waiter();

               AbstractFuture.Waiter var5;
               do {
                  var4.setNext(var6);
                  if (ATOMIC_HELPER.casWaiters(this, var6, var4)) {
                     do {
                        LockSupport.park(this);
                        if (Thread.interrupted()) {
                           this.removeWaiter(var4);
                           throw new InterruptedException();
                        }

                        var1 = this.value;
                        if (var1 != null) {
                           var2 = true;
                        } else {
                           var2 = false;
                        }

                        if (!(var1 instanceof AbstractFuture.SetFuture)) {
                           var3 = true;
                        } else {
                           var3 = false;
                        }
                     } while(!(var2 & var3));

                     return this.getDoneValue(var1);
                  }

                  var5 = this.waiters;
                  var6 = var5;
               } while(var5 != AbstractFuture.Waiter.TOMBSTONE);
            }

            return this.getDoneValue(this.value);
         }
      } else {
         throw new InterruptedException();
      }
   }

   public final Object get(long var1, TimeUnit var3) throws InterruptedException, TimeoutException, ExecutionException {
      long var4 = var3.toNanos(var1);
      if (!Thread.interrupted()) {
         Object var6 = this.value;
         boolean var7;
         if (var6 != null) {
            var7 = true;
         } else {
            var7 = false;
         }

         boolean var8;
         if (!(var6 instanceof AbstractFuture.SetFuture)) {
            var8 = true;
         } else {
            var8 = false;
         }

         if (var7 & var8) {
            return this.getDoneValue(var6);
         } else {
            long var9;
            if (var4 > 0L) {
               var9 = System.nanoTime() + var4;
            } else {
               var9 = 0L;
            }

            long var11 = var4;
            if (var4 >= 1000L) {
               label131: {
                  AbstractFuture.Waiter var18 = this.waiters;
                  if (var18 != AbstractFuture.Waiter.TOMBSTONE) {
                     AbstractFuture.Waiter var13 = new AbstractFuture.Waiter();

                     AbstractFuture.Waiter var14;
                     do {
                        var13.setNext(var18);
                        if (ATOMIC_HELPER.casWaiters(this, var18, var13)) {
                           do {
                              LockSupport.parkNanos(this, var4);
                              if (Thread.interrupted()) {
                                 this.removeWaiter(var13);
                                 throw new InterruptedException();
                              }

                              var6 = this.value;
                              if (var6 != null) {
                                 var7 = true;
                              } else {
                                 var7 = false;
                              }

                              if (!(var6 instanceof AbstractFuture.SetFuture)) {
                                 var8 = true;
                              } else {
                                 var8 = false;
                              }

                              if (var7 & var8) {
                                 return this.getDoneValue(var6);
                              }

                              var11 = var9 - System.nanoTime();
                              var4 = var11;
                           } while(var11 >= 1000L);

                           this.removeWaiter(var13);
                           break label131;
                        }

                        var14 = this.waiters;
                        var18 = var14;
                     } while(var14 != AbstractFuture.Waiter.TOMBSTONE);
                  }

                  return this.getDoneValue(this.value);
               }
            }

            while(var11 > 0L) {
               var6 = this.value;
               if (var6 != null) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               if (!(var6 instanceof AbstractFuture.SetFuture)) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               if (var7 & var8) {
                  return this.getDoneValue(var6);
               }

               if (Thread.interrupted()) {
                  throw new InterruptedException();
               }

               var11 = var9 - System.nanoTime();
            }

            String var22 = this.toString();
            String var15 = var3.toString().toLowerCase(Locale.ROOT);
            StringBuilder var19 = new StringBuilder();
            var19.append("Waited ");
            var19.append(var1);
            var19.append(" ");
            var19.append(var3.toString().toLowerCase(Locale.ROOT));
            String var23 = var19.toString();
            String var20 = var23;
            StringBuilder var17;
            if (var11 + 1000L < 0L) {
               var19 = new StringBuilder();
               var19.append(var23);
               var19.append(" (plus ");
               var20 = var19.toString();
               var11 = -var11;
               var1 = var3.convert(var11, TimeUnit.NANOSECONDS);
               var11 -= var3.toNanos(var1);
               long var24;
               int var21 = (var24 = var1 - 0L) == 0L ? 0 : (var24 < 0L ? -1 : 1);
               if (var21 != 0 && var11 <= 1000L) {
                  var7 = false;
               } else {
                  var7 = true;
               }

               String var16 = var20;
               if (var21 > 0) {
                  var17 = new StringBuilder();
                  var17.append(var20);
                  var17.append(var1);
                  var17.append(" ");
                  var17.append(var15);
                  var20 = var17.toString();
                  var16 = var20;
                  if (var7) {
                     var17 = new StringBuilder();
                     var17.append(var20);
                     var17.append(",");
                     var16 = var17.toString();
                  }

                  var19 = new StringBuilder();
                  var19.append(var16);
                  var19.append(" ");
                  var16 = var19.toString();
               }

               var20 = var16;
               if (var7) {
                  var19 = new StringBuilder();
                  var19.append(var16);
                  var19.append(var11);
                  var19.append(" nanoseconds ");
                  var20 = var19.toString();
               }

               var17 = new StringBuilder();
               var17.append(var20);
               var17.append("delay)");
               var20 = var17.toString();
            }

            if (this.isDone()) {
               var17 = new StringBuilder();
               var17.append(var20);
               var17.append(" but future completed as timeout expired");
               throw new TimeoutException(var17.toString());
            } else {
               var17 = new StringBuilder();
               var17.append(var20);
               var17.append(" for ");
               var17.append(var22);
               throw new TimeoutException(var17.toString());
            }
         }
      } else {
         throw new InterruptedException();
      }
   }

   protected void interruptTask() {
   }

   public final boolean isCancelled() {
      return this.value instanceof AbstractFuture.Cancellation;
   }

   public final boolean isDone() {
      Object var1 = this.value;
      boolean var2 = false;
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (!(var1 instanceof AbstractFuture.SetFuture)) {
         var2 = true;
      }

      return var3 & var2;
   }

   protected String pendingToString() {
      Object var1 = this.value;
      if (var1 instanceof AbstractFuture.SetFuture) {
         StringBuilder var2 = new StringBuilder();
         var2.append("setFuture=[");
         var2.append(this.userObjectToString(((AbstractFuture.SetFuture)var1).future));
         var2.append("]");
         return var2.toString();
      } else if (this instanceof ScheduledFuture) {
         StringBuilder var3 = new StringBuilder();
         var3.append("remaining delay=[");
         var3.append(((ScheduledFuture)this).getDelay(TimeUnit.MILLISECONDS));
         var3.append(" ms]");
         return var3.toString();
      } else {
         return null;
      }
   }

   protected boolean set(Object var1) {
      Object var2 = var1;
      if (var1 == null) {
         var2 = NULL;
      }

      if (ATOMIC_HELPER.casValue(this, (Object)null, var2)) {
         complete(this);
         return true;
      } else {
         return false;
      }
   }

   protected boolean setException(Throwable var1) {
      AbstractFuture.Failure var2 = new AbstractFuture.Failure((Throwable)checkNotNull(var1));
      if (ATOMIC_HELPER.casValue(this, (Object)null, var2)) {
         complete(this);
         return true;
      } else {
         return false;
      }
   }

   protected boolean setFuture(ListenableFuture var1) {
      checkNotNull(var1);
      Object var2 = this.value;
      Object var3 = var2;
      if (var2 == null) {
         if (var1.isDone()) {
            Object var7 = getFutureValue(var1);
            if (ATOMIC_HELPER.casValue(this, (Object)null, var7)) {
               complete(this);
               return true;
            }

            return false;
         }

         AbstractFuture.SetFuture var9 = new AbstractFuture.SetFuture(this, var1);
         if (ATOMIC_HELPER.casValue(this, (Object)null, var9)) {
            try {
               var1.addListener(var9, DirectExecutor.INSTANCE);
            } catch (Throwable var5) {
               Throwable var8 = var5;

               AbstractFuture.Failure var6;
               try {
                  var6 = new AbstractFuture.Failure(var8);
               } catch (Throwable var4) {
                  var6 = AbstractFuture.Failure.FALLBACK_INSTANCE;
               }

               ATOMIC_HELPER.casValue(this, var9, var6);
            }

            return true;
         }

         var3 = this.value;
      }

      if (var3 instanceof AbstractFuture.Cancellation) {
         var1.cancel(((AbstractFuture.Cancellation)var3).wasInterrupted);
      }

      return false;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("[status=");
      if (this.isCancelled()) {
         var1.append("CANCELLED");
      } else if (this.isDone()) {
         this.addDoneString(var1);
      } else {
         String var5;
         try {
            var5 = this.pendingToString();
         } catch (RuntimeException var4) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Exception thrown from implementation: ");
            var2.append(var4.getClass());
            var5 = var2.toString();
         }

         if (var5 != null && !var5.isEmpty()) {
            var1.append("PENDING, info=[");
            var1.append(var5);
            var1.append("]");
         } else if (this.isDone()) {
            this.addDoneString(var1);
         } else {
            var1.append("PENDING");
         }
      }

      var1.append("]");
      return var1.toString();
   }

   private abstract static class AtomicHelper {
      private AtomicHelper() {
      }

      // $FF: synthetic method
      AtomicHelper(Object var1) {
         this();
      }

      abstract boolean casListeners(AbstractFuture var1, AbstractFuture.Listener var2, AbstractFuture.Listener var3);

      abstract boolean casValue(AbstractFuture var1, Object var2, Object var3);

      abstract boolean casWaiters(AbstractFuture var1, AbstractFuture.Waiter var2, AbstractFuture.Waiter var3);

      abstract void putNext(AbstractFuture.Waiter var1, AbstractFuture.Waiter var2);

      abstract void putThread(AbstractFuture.Waiter var1, Thread var2);
   }

   private static final class Cancellation {
      static final AbstractFuture.Cancellation CAUSELESS_CANCELLED;
      static final AbstractFuture.Cancellation CAUSELESS_INTERRUPTED;
      final Throwable cause;
      final boolean wasInterrupted;

      static {
         if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
            CAUSELESS_CANCELLED = null;
            CAUSELESS_INTERRUPTED = null;
         } else {
            CAUSELESS_CANCELLED = new AbstractFuture.Cancellation(false, (Throwable)null);
            CAUSELESS_INTERRUPTED = new AbstractFuture.Cancellation(true, (Throwable)null);
         }

      }

      Cancellation(boolean var1, Throwable var2) {
         this.wasInterrupted = var1;
         this.cause = var2;
      }
   }

   private static final class Failure {
      static final AbstractFuture.Failure FALLBACK_INSTANCE = new AbstractFuture.Failure(new Throwable("Failure occurred while trying to finish a future.") {
         public Throwable fillInStackTrace() {
            synchronized(this){}
            return this;
         }
      });
      final Throwable exception;

      Failure(Throwable var1) {
         this.exception = (Throwable)AbstractFuture.checkNotNull(var1);
      }
   }

   private static final class Listener {
      static final AbstractFuture.Listener TOMBSTONE = new AbstractFuture.Listener((Runnable)null, (Executor)null);
      final Executor executor;
      AbstractFuture.Listener next;
      final Runnable task;

      Listener(Runnable var1, Executor var2) {
         this.task = var1;
         this.executor = var2;
      }
   }

   private static final class SafeAtomicHelper extends AbstractFuture.AtomicHelper {
      final AtomicReferenceFieldUpdater listenersUpdater;
      final AtomicReferenceFieldUpdater valueUpdater;
      final AtomicReferenceFieldUpdater waiterNextUpdater;
      final AtomicReferenceFieldUpdater waiterThreadUpdater;
      final AtomicReferenceFieldUpdater waitersUpdater;

      SafeAtomicHelper(AtomicReferenceFieldUpdater var1, AtomicReferenceFieldUpdater var2, AtomicReferenceFieldUpdater var3, AtomicReferenceFieldUpdater var4, AtomicReferenceFieldUpdater var5) {
         super(null);
         this.waiterThreadUpdater = var1;
         this.waiterNextUpdater = var2;
         this.waitersUpdater = var3;
         this.listenersUpdater = var4;
         this.valueUpdater = var5;
      }

      boolean casListeners(AbstractFuture var1, AbstractFuture.Listener var2, AbstractFuture.Listener var3) {
         return this.listenersUpdater.compareAndSet(var1, var2, var3);
      }

      boolean casValue(AbstractFuture var1, Object var2, Object var3) {
         return this.valueUpdater.compareAndSet(var1, var2, var3);
      }

      boolean casWaiters(AbstractFuture var1, AbstractFuture.Waiter var2, AbstractFuture.Waiter var3) {
         return this.waitersUpdater.compareAndSet(var1, var2, var3);
      }

      void putNext(AbstractFuture.Waiter var1, AbstractFuture.Waiter var2) {
         this.waiterNextUpdater.lazySet(var1, var2);
      }

      void putThread(AbstractFuture.Waiter var1, Thread var2) {
         this.waiterThreadUpdater.lazySet(var1, var2);
      }
   }

   private static final class SetFuture implements Runnable {
      final ListenableFuture future;
      final AbstractFuture owner;

      SetFuture(AbstractFuture var1, ListenableFuture var2) {
         this.owner = var1;
         this.future = var2;
      }

      public void run() {
         if (this.owner.value == this) {
            Object var1 = AbstractFuture.getFutureValue(this.future);
            if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, var1)) {
               AbstractFuture.complete(this.owner);
            }

         }
      }
   }

   private static final class SynchronizedHelper extends AbstractFuture.AtomicHelper {
      SynchronizedHelper() {
         super(null);
      }

      boolean casListeners(AbstractFuture var1, AbstractFuture.Listener var2, AbstractFuture.Listener var3) {
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label123: {
            try {
               if (var1.listeners == var2) {
                  var1.listeners = var3;
                  return true;
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label123;
            }

            label117:
            try {
               return false;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label117;
            }
         }

         while(true) {
            Throwable var16 = var10000;

            try {
               throw var16;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               continue;
            }
         }
      }

      boolean casValue(AbstractFuture var1, Object var2, Object var3) {
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label123: {
            try {
               if (var1.value == var2) {
                  var1.value = var3;
                  return true;
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label123;
            }

            label117:
            try {
               return false;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label117;
            }
         }

         while(true) {
            Throwable var16 = var10000;

            try {
               throw var16;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               continue;
            }
         }
      }

      boolean casWaiters(AbstractFuture var1, AbstractFuture.Waiter var2, AbstractFuture.Waiter var3) {
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label123: {
            try {
               if (var1.waiters == var2) {
                  var1.waiters = var3;
                  return true;
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label123;
            }

            label117:
            try {
               return false;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label117;
            }
         }

         while(true) {
            Throwable var16 = var10000;

            try {
               throw var16;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               continue;
            }
         }
      }

      void putNext(AbstractFuture.Waiter var1, AbstractFuture.Waiter var2) {
         var1.next = var2;
      }

      void putThread(AbstractFuture.Waiter var1, Thread var2) {
         var1.thread = var2;
      }
   }

   private static final class Waiter {
      static final AbstractFuture.Waiter TOMBSTONE = new AbstractFuture.Waiter(false);
      volatile AbstractFuture.Waiter next;
      volatile Thread thread;

      Waiter() {
         AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
      }

      Waiter(boolean var1) {
      }

      void setNext(AbstractFuture.Waiter var1) {
         AbstractFuture.ATOMIC_HELPER.putNext(this, var1);
      }

      void unpark() {
         Thread var1 = this.thread;
         if (var1 != null) {
            this.thread = null;
            LockSupport.unpark(var1);
         }

      }
   }
}
