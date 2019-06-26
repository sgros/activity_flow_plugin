package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.StreamResetException;

public final class StreamAllocation {
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   public final Address address;
   private final Object callStackTrace;
   private boolean canceled;
   private HttpCodec codec;
   private RealConnection connection;
   private final ConnectionPool connectionPool;
   private int refusedStreamCount;
   private boolean released;
   private Route route;
   private final RouteSelector routeSelector;

   static {
      boolean var0;
      if (!StreamAllocation.class.desiredAssertionStatus()) {
         var0 = true;
      } else {
         var0 = false;
      }

      $assertionsDisabled = var0;
   }

   public StreamAllocation(ConnectionPool var1, Address var2, Object var3) {
      this.connectionPool = var1;
      this.address = var2;
      this.routeSelector = new RouteSelector(var2, this.routeDatabase());
      this.callStackTrace = var3;
   }

   private Socket deallocate(boolean var1, boolean var2, boolean var3) {
      if (!$assertionsDisabled && !Thread.holdsLock(this.connectionPool)) {
         throw new AssertionError();
      } else {
         if (var3) {
            this.codec = null;
         }

         if (var2) {
            this.released = true;
         }

         Object var4 = null;
         Object var5 = null;
         Socket var6 = (Socket)var4;
         if (this.connection != null) {
            if (var1) {
               this.connection.noNewStreams = true;
            }

            var6 = (Socket)var4;
            if (this.codec == null) {
               if (!this.released) {
                  var6 = (Socket)var4;
                  if (!this.connection.noNewStreams) {
                     return var6;
                  }
               }

               this.release(this.connection);
               var6 = (Socket)var5;
               if (this.connection.allocations.isEmpty()) {
                  this.connection.idleAtNanos = System.nanoTime();
                  var6 = (Socket)var5;
                  if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                     var6 = this.connection.socket();
                  }
               }

               this.connection = null;
            }
         }

         return var6;
      }
   }

   private RealConnection findConnection(int var1, int var2, int var3, boolean var4) throws IOException {
      ConnectionPool var5 = this.connectionPool;
      synchronized(var5){}

      Throwable var10000;
      boolean var10001;
      Throwable var250;
      label2208: {
         IllegalStateException var255;
         try {
            if (this.released) {
               var255 = new IllegalStateException("released");
               throw var255;
            }
         } catch (Throwable var247) {
            var10000 = var247;
            var10001 = false;
            break label2208;
         }

         try {
            if (this.codec != null) {
               var255 = new IllegalStateException("codec != null");
               throw var255;
            }
         } catch (Throwable var246) {
            var10000 = var246;
            var10001 = false;
            break label2208;
         }

         IOException var254;
         try {
            if (this.canceled) {
               var254 = new IOException("Canceled");
               throw var254;
            }
         } catch (Throwable var248) {
            var10000 = var248;
            var10001 = false;
            break label2208;
         }

         RealConnection var6;
         try {
            var6 = this.connection;
         } catch (Throwable var245) {
            var10000 = var245;
            var10001 = false;
            break label2208;
         }

         label2209: {
            label2226: {
               if (var6 != null) {
                  try {
                     if (!var6.noNewStreams) {
                        return var6;
                     }
                  } catch (Throwable var241) {
                     var10000 = var241;
                     var10001 = false;
                     break label2208;
                  }
               }

               try {
                  Internal.instance.get(this.connectionPool, this.address, this);
                  if (this.connection != null) {
                     var6 = this.connection;
                     return var6;
                  }
               } catch (Throwable var244) {
                  var10000 = var244;
                  var10001 = false;
                  break label2208;
               }

               Route var7;
               try {
                  var7 = this.route;
               } catch (Throwable var237) {
                  var10000 = var237;
                  var10001 = false;
                  break label2208;
               }

               Route var251 = var7;
               if (var7 == null) {
                  var251 = this.routeSelector.next();
               }

               ConnectionPool var252 = this.connectionPool;
               synchronized(var252){}

               RealConnection var249;
               try {
                  this.route = var251;
                  this.refusedStreamCount = 0;
                  var249 = new RealConnection(this.connectionPool, var251);
                  this.acquire(var249);
                  if (this.canceled) {
                     var254 = new IOException("Canceled");
                     throw var254;
                  }
               } catch (Throwable var243) {
                  var10000 = var243;
                  var10001 = false;
                  break label2226;
               }

               try {
                  ;
               } catch (Throwable var240) {
                  var10000 = var240;
                  var10001 = false;
                  break label2226;
               }

               var249.connect(var1, var2, var3, var4);
               this.routeDatabase().connected(var249.route());
               Socket var253 = null;
               ConnectionPool var8 = this.connectionPool;
               synchronized(var8){}

               try {
                  Internal.instance.put(this.connectionPool, var249);
               } catch (Throwable var239) {
                  var10000 = var239;
                  var10001 = false;
                  break label2209;
               }

               var6 = var249;

               try {
                  if (var249.isMultiplexed()) {
                     var253 = Internal.instance.deduplicate(this.connectionPool, this.address, this);
                     var6 = this.connection;
                  }
               } catch (Throwable var242) {
                  var10000 = var242;
                  var10001 = false;
                  break label2209;
               }

               try {
                  ;
               } catch (Throwable var238) {
                  var10000 = var238;
                  var10001 = false;
                  break label2209;
               }

               Util.closeQuietly(var253);
               return var6;
            }

            while(true) {
               var250 = var10000;

               try {
                  throw var250;
               } catch (Throwable var234) {
                  var10000 = var234;
                  var10001 = false;
                  continue;
               }
            }
         }

         while(true) {
            var250 = var10000;

            try {
               throw var250;
            } catch (Throwable var236) {
               var10000 = var236;
               var10001 = false;
               continue;
            }
         }
      }

      while(true) {
         var250 = var10000;

         try {
            throw var250;
         } catch (Throwable var235) {
            var10000 = var235;
            var10001 = false;
            continue;
         }
      }
   }

   private RealConnection findHealthyConnection(int var1, int var2, int var3, boolean var4, boolean var5) throws IOException {
      while(true) {
         RealConnection var6 = this.findConnection(var1, var2, var3, var4);
         ConnectionPool var7 = this.connectionPool;
         synchronized(var7){}

         Throwable var10000;
         boolean var10001;
         label170: {
            try {
               if (var6.successCount == 0) {
                  return var6;
               }
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label170;
            }

            try {
               ;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label170;
            }

            if (!var6.isHealthy(var5)) {
               this.noNewStreams();
               continue;
            }

            return var6;
         }

         while(true) {
            Throwable var20 = var10000;

            try {
               throw var20;
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               continue;
            }
         }
      }
   }

   private void release(RealConnection var1) {
      int var2 = 0;

      for(int var3 = var1.allocations.size(); var2 < var3; ++var2) {
         if (((Reference)var1.allocations.get(var2)).get() == this) {
            var1.allocations.remove(var2);
            return;
         }
      }

      throw new IllegalStateException();
   }

   private RouteDatabase routeDatabase() {
      return Internal.instance.routeDatabase(this.connectionPool);
   }

   public void acquire(RealConnection var1) {
      if (!$assertionsDisabled && !Thread.holdsLock(this.connectionPool)) {
         throw new AssertionError();
      } else if (this.connection != null) {
         throw new IllegalStateException();
      } else {
         this.connection = var1;
         var1.allocations.add(new StreamAllocation.StreamAllocationReference(this, this.callStackTrace));
      }
   }

   public void cancel() {
      // $FF: Couldn't be decompiled
   }

   public HttpCodec codec() {
      // $FF: Couldn't be decompiled
   }

   public RealConnection connection() {
      synchronized(this){}

      RealConnection var1;
      try {
         var1 = this.connection;
      } finally {
         ;
      }

      return var1;
   }

   public boolean hasMoreRoutes() {
      boolean var1;
      if (this.route == null && !this.routeSelector.hasNext()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public HttpCodec newStream(OkHttpClient param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public void noNewStreams() {
      // $FF: Couldn't be decompiled
   }

   public void release() {
      // $FF: Couldn't be decompiled
   }

   public Socket releaseAndAcquire(RealConnection var1) {
      if (!$assertionsDisabled && !Thread.holdsLock(this.connectionPool)) {
         throw new AssertionError();
      } else if (this.codec == null && this.connection.allocations.size() == 1) {
         Reference var2 = (Reference)this.connection.allocations.get(0);
         Socket var3 = this.deallocate(true, false, false);
         this.connection = var1;
         var1.allocations.add(var2);
         return var3;
      } else {
         throw new IllegalStateException();
      }
   }

   public void streamFailed(IOException var1) {
      boolean var2 = false;
      ConnectionPool var3 = this.connectionPool;
      synchronized(var3){}

      Socket var139;
      label1222: {
         Throwable var10000;
         boolean var10001;
         label1217: {
            boolean var4;
            label1223: {
               label1224: {
                  StreamResetException var137;
                  try {
                     if (!(var1 instanceof StreamResetException)) {
                        break label1224;
                     }

                     var137 = (StreamResetException)var1;
                     if (var137.errorCode == ErrorCode.REFUSED_STREAM) {
                        ++this.refusedStreamCount;
                     }
                  } catch (Throwable var136) {
                     var10000 = var136;
                     var10001 = false;
                     break label1217;
                  }

                  label1225: {
                     try {
                        if (var137.errorCode != ErrorCode.REFUSED_STREAM) {
                           break label1225;
                        }
                     } catch (Throwable var133) {
                        var10000 = var133;
                        var10001 = false;
                        break label1217;
                     }

                     var4 = var2;

                     try {
                        if (this.refusedStreamCount <= 1) {
                           break label1223;
                        }
                     } catch (Throwable var131) {
                        var10000 = var131;
                        var10001 = false;
                        break label1217;
                     }
                  }

                  var4 = true;

                  try {
                     this.route = null;
                     break label1223;
                  } catch (Throwable var130) {
                     var10000 = var130;
                     var10001 = false;
                     break label1217;
                  }
               }

               var4 = var2;

               label1226: {
                  try {
                     if (this.connection == null) {
                        break label1223;
                     }

                     if (!this.connection.isMultiplexed()) {
                        break label1226;
                     }
                  } catch (Throwable var135) {
                     var10000 = var135;
                     var10001 = false;
                     break label1217;
                  }

                  var4 = var2;

                  try {
                     if (!(var1 instanceof ConnectionShutdownException)) {
                        break label1223;
                     }
                  } catch (Throwable var132) {
                     var10000 = var132;
                     var10001 = false;
                     break label1217;
                  }
               }

               var2 = true;
               var4 = var2;

               label1201: {
                  try {
                     if (this.connection.successCount != 0) {
                        break label1223;
                     }

                     if (this.route == null) {
                        break label1201;
                     }
                  } catch (Throwable var134) {
                     var10000 = var134;
                     var10001 = false;
                     break label1217;
                  }

                  if (var1 != null) {
                     try {
                        this.routeSelector.connectFailed(this.route, var1);
                     } catch (Throwable var129) {
                        var10000 = var129;
                        var10001 = false;
                        break label1217;
                     }
                  }
               }

               try {
                  this.route = null;
               } catch (Throwable var128) {
                  var10000 = var128;
                  var10001 = false;
                  break label1217;
               }

               var4 = var2;
            }

            label1174:
            try {
               var139 = this.deallocate(var4, false, true);
               break label1222;
            } catch (Throwable var127) {
               var10000 = var127;
               var10001 = false;
               break label1174;
            }
         }

         while(true) {
            Throwable var138 = var10000;

            try {
               throw var138;
            } catch (Throwable var126) {
               var10000 = var126;
               var10001 = false;
               continue;
            }
         }
      }

      Util.closeQuietly(var139);
   }

   public void streamFinished(boolean var1, HttpCodec var2) {
      Throwable var10000;
      boolean var10001;
      label259: {
         label255: {
            ConnectionPool var3 = this.connectionPool;
            synchronized(var3){}
            if (var2 != null) {
               try {
                  if (var2 == this.codec) {
                     break label255;
                  }
               } catch (Throwable var35) {
                  var10000 = var35;
                  var10001 = false;
                  break label259;
               }
            }

            try {
               StringBuilder var5 = new StringBuilder();
               IllegalStateException var4 = new IllegalStateException(var5.append("expected ").append(this.codec).append(" but was ").append(var2).toString());
               throw var4;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label259;
            }
         }

         if (!var1) {
            try {
               RealConnection var36 = this.connection;
               ++var36.successCount;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label259;
            }
         }

         Socket var38;
         try {
            var38 = this.deallocate(var1, false, true);
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label259;
         }

         Util.closeQuietly(var38);
         return;
      }

      while(true) {
         Throwable var37 = var10000;

         try {
            throw var37;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            continue;
         }
      }
   }

   public String toString() {
      RealConnection var1 = this.connection();
      String var2;
      if (var1 != null) {
         var2 = var1.toString();
      } else {
         var2 = this.address.toString();
      }

      return var2;
   }

   public static final class StreamAllocationReference extends WeakReference {
      public final Object callStackTrace;

      StreamAllocationReference(StreamAllocation var1, Object var2) {
         super(var1);
         this.callStackTrace = var2;
      }
   }
}
