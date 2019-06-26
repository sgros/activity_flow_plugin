package okhttp3;

import java.lang.ref.Reference;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.platform.Platform;

public final class ConnectionPool {
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   private static final Executor executor;
   private final Runnable cleanupRunnable;
   boolean cleanupRunning;
   private final Deque connections;
   private final long keepAliveDurationNs;
   private final int maxIdleConnections;
   final RouteDatabase routeDatabase;

   static {
      boolean var0;
      if (!ConnectionPool.class.desiredAssertionStatus()) {
         var0 = true;
      } else {
         var0 = false;
      }

      $assertionsDisabled = var0;
      executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
   }

   public ConnectionPool() {
      this(5, 5L, TimeUnit.MINUTES);
   }

   public ConnectionPool(int var1, long var2, TimeUnit var4) {
      this.cleanupRunnable = new Runnable() {
         public void run() {
            while(true) {
               long var1 = ConnectionPool.this.cleanup(System.nanoTime());
               if (var1 == -1L) {
                  return;
               }

               if (var1 > 0L) {
                  long var3 = var1 / 1000000L;
                  ConnectionPool var5 = ConnectionPool.this;
                  synchronized(var5){}

                  Throwable var10000;
                  boolean var10001;
                  label143: {
                     try {
                        try {
                           ConnectionPool.this.wait(var3, (int)(var1 - var3 * 1000000L));
                        } catch (InterruptedException var19) {
                        }
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label143;
                     }

                     label140:
                     try {
                        continue;
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label140;
                     }
                  }

                  while(true) {
                     Throwable var6 = var10000;

                     try {
                        throw var6;
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        continue;
                     }
                  }
               }
            }
         }
      };
      this.connections = new ArrayDeque();
      this.routeDatabase = new RouteDatabase();
      this.maxIdleConnections = var1;
      this.keepAliveDurationNs = var4.toNanos(var2);
      if (var2 <= 0L) {
         throw new IllegalArgumentException("keepAliveDuration <= 0: " + var2);
      }
   }

   private int pruneAndGetAllocationCount(RealConnection var1, long var2) {
      List var4 = var1.allocations;
      int var5 = 0;

      while(true) {
         if (var5 < var4.size()) {
            Reference var6 = (Reference)var4.get(var5);
            if (var6.get() != null) {
               ++var5;
               continue;
            }

            StreamAllocation.StreamAllocationReference var7 = (StreamAllocation.StreamAllocationReference)var6;
            String var8 = "A connection to " + var1.route().address().url() + " was leaked. Did you forget to close a response body?";
            Platform.get().logCloseableLeak(var8, var7.callStackTrace);
            var4.remove(var5);
            var1.noNewStreams = true;
            if (!var4.isEmpty()) {
               continue;
            }

            var1.idleAtNanos = var2 - this.keepAliveDurationNs;
            var5 = 0;
            break;
         }

         var5 = var4.size();
         break;
      }

      return var5;
   }

   long cleanup(long var1) {
      int var3 = 0;
      int var4 = 0;
      RealConnection var5 = null;
      long var6 = Long.MIN_VALUE;
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label1058: {
         Iterator var8;
         try {
            var8 = this.connections.iterator();
         } catch (Throwable var121) {
            var10000 = var121;
            var10001 = false;
            break label1058;
         }

         label1057:
         while(true) {
            RealConnection var9;
            while(true) {
               label1077: {
                  try {
                     if (var8.hasNext()) {
                        var9 = (RealConnection)var8.next();
                        if (this.pruneAndGetAllocationCount(var9, var1) <= 0) {
                           break;
                        }
                        break label1077;
                     }
                  } catch (Throwable var122) {
                     var10000 = var122;
                     var10001 = false;
                     break label1057;
                  }

                  label1078: {
                     try {
                        if (var6 < this.keepAliveDurationNs && var4 <= this.maxIdleConnections) {
                           break label1078;
                        }
                     } catch (Throwable var119) {
                        var10000 = var119;
                        var10001 = false;
                        break label1057;
                     }

                     try {
                        this.connections.remove(var5);
                     } catch (Throwable var118) {
                        var10000 = var118;
                        var10001 = false;
                        break label1057;
                     }

                     Util.closeQuietly(var5.socket());
                     var1 = 0L;
                     return var1;
                  }

                  if (var4 > 0) {
                     try {
                        var1 = this.keepAliveDurationNs - var6;
                     } catch (Throwable var114) {
                        var10000 = var114;
                        var10001 = false;
                        break label1057;
                     }
                  } else {
                     if (var3 > 0) {
                        try {
                           var1 = this.keepAliveDurationNs;
                        } catch (Throwable var115) {
                           var10000 = var115;
                           var10001 = false;
                           break label1057;
                        }
                     } else {
                        try {
                           this.cleanupRunning = false;
                        } catch (Throwable var117) {
                           var10000 = var117;
                           var10001 = false;
                           break label1057;
                        }

                        var1 = -1L;

                        try {
                           ;
                        } catch (Throwable var116) {
                           var10000 = var116;
                           var10001 = false;
                           break label1057;
                        }
                     }

                     return var1;
                  }

                  return var1;
               }

               ++var3;
            }

            int var10 = var4 + 1;

            long var11;
            try {
               var11 = var1 - var9.idleAtNanos;
            } catch (Throwable var120) {
               var10000 = var120;
               var10001 = false;
               break;
            }

            var4 = var10;
            if (var11 > var6) {
               var6 = var11;
               var5 = var9;
               var4 = var10;
            }
         }
      }

      while(true) {
         Throwable var123 = var10000;

         try {
            throw var123;
         } catch (Throwable var113) {
            var10000 = var113;
            var10001 = false;
            continue;
         }
      }
   }

   boolean connectionBecameIdle(RealConnection var1) {
      if (!$assertionsDisabled && !Thread.holdsLock(this)) {
         throw new AssertionError();
      } else {
         boolean var2;
         if (!var1.noNewStreams && this.maxIdleConnections != 0) {
            this.notifyAll();
            var2 = false;
         } else {
            this.connections.remove(var1);
            var2 = true;
         }

         return var2;
      }
   }

   public int connectionCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.connections.size();
      } finally {
         ;
      }

      return var1;
   }

   Socket deduplicate(Address var1, StreamAllocation var2) {
      if (!$assertionsDisabled && !Thread.holdsLock(this)) {
         throw new AssertionError();
      } else {
         Iterator var3 = this.connections.iterator();

         Socket var5;
         while(true) {
            if (var3.hasNext()) {
               RealConnection var4 = (RealConnection)var3.next();
               if (!var4.isEligible(var1) || !var4.isMultiplexed() || var4 == var2.connection()) {
                  continue;
               }

               var5 = var2.releaseAndAcquire(var4);
               break;
            }

            var5 = null;
            break;
         }

         return var5;
      }
   }

   public void evictAll() {
      ArrayList var1 = new ArrayList();
      synchronized(this){}

      Iterator var2;
      label283: {
         Throwable var10000;
         boolean var10001;
         label278: {
            try {
               var2 = this.connections.iterator();
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label278;
            }

            while(true) {
               try {
                  while(var2.hasNext()) {
                     RealConnection var3 = (RealConnection)var2.next();
                     if (var3.allocations.isEmpty()) {
                        var3.noNewStreams = true;
                        var1.add(var3);
                        var2.remove();
                     }
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               try {
                  break label283;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break;
               }
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               continue;
            }
         }
      }

      var2 = var1.iterator();

      while(var2.hasNext()) {
         Util.closeQuietly(((RealConnection)var2.next()).socket());
      }

   }

   RealConnection get(Address var1, StreamAllocation var2) {
      if (!$assertionsDisabled && !Thread.holdsLock(this)) {
         throw new AssertionError();
      } else {
         Iterator var3 = this.connections.iterator();

         RealConnection var5;
         while(true) {
            if (var3.hasNext()) {
               RealConnection var4 = (RealConnection)var3.next();
               if (!var4.isEligible(var1)) {
                  continue;
               }

               var2.acquire(var4);
               var5 = var4;
               break;
            }

            var5 = null;
            break;
         }

         return var5;
      }
   }

   public int idleConnectionCount() {
      synchronized(this){}
      int var1 = 0;

      Throwable var10000;
      label83: {
         boolean var10001;
         Iterator var2;
         try {
            var2 = this.connections.iterator();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label83;
         }

         while(true) {
            boolean var3;
            try {
               if (!var2.hasNext()) {
                  return var1;
               }

               var3 = ((RealConnection)var2.next()).allocations.isEmpty();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }

            if (var3) {
               ++var1;
            }
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   void put(RealConnection var1) {
      if (!$assertionsDisabled && !Thread.holdsLock(this)) {
         throw new AssertionError();
      } else {
         if (!this.cleanupRunning) {
            this.cleanupRunning = true;
            executor.execute(this.cleanupRunnable);
         }

         this.connections.add(var1);
      }
   }
}
