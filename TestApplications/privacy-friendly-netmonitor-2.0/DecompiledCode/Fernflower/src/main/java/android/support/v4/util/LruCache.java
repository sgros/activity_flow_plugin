package android.support.v4.util;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class LruCache {
   private int createCount;
   private int evictionCount;
   private int hitCount;
   private final LinkedHashMap map;
   private int maxSize;
   private int missCount;
   private int putCount;
   private int size;

   public LruCache(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("maxSize <= 0");
      } else {
         this.maxSize = var1;
         this.map = new LinkedHashMap(0, 0.75F, true);
      }
   }

   private int safeSizeOf(Object var1, Object var2) {
      int var3 = this.sizeOf(var1, var2);
      if (var3 < 0) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Negative size: ");
         var4.append(var1);
         var4.append("=");
         var4.append(var2);
         throw new IllegalStateException(var4.toString());
      } else {
         return var3;
      }
   }

   protected Object create(Object var1) {
      return null;
   }

   public final int createCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.createCount;
      } finally {
         ;
      }

      return var1;
   }

   protected void entryRemoved(boolean var1, Object var2, Object var3, Object var4) {
   }

   public final void evictAll() {
      this.trimToSize(-1);
   }

   public final int evictionCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.evictionCount;
      } finally {
         ;
      }

      return var1;
   }

   public final Object get(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("key == null");
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         Throwable var94;
         label843: {
            Object var2;
            try {
               var2 = this.map.get(var1);
            } catch (Throwable var93) {
               var10000 = var93;
               var10001 = false;
               break label843;
            }

            if (var2 != null) {
               label836:
               try {
                  ++this.hitCount;
                  return var2;
               } catch (Throwable var91) {
                  var10000 = var91;
                  var10001 = false;
                  break label836;
               }
            } else {
               label849: {
                  try {
                     ++this.missCount;
                  } catch (Throwable var92) {
                     var10000 = var92;
                     var10001 = false;
                     break label849;
                  }

                  var2 = this.create(var1);
                  if (var2 == null) {
                     return null;
                  }

                  synchronized(this){}

                  Object var3;
                  label850: {
                     label851: {
                        try {
                           ++this.createCount;
                           var3 = this.map.put(var1, var2);
                        } catch (Throwable var90) {
                           var10000 = var90;
                           var10001 = false;
                           break label851;
                        }

                        if (var3 != null) {
                           try {
                              this.map.put(var1, var3);
                           } catch (Throwable var89) {
                              var10000 = var89;
                              var10001 = false;
                              break label851;
                           }
                        } else {
                           try {
                              this.size += this.safeSizeOf(var1, var2);
                           } catch (Throwable var88) {
                              var10000 = var88;
                              var10001 = false;
                              break label851;
                           }
                        }

                        label819:
                        try {
                           break label850;
                        } catch (Throwable var87) {
                           var10000 = var87;
                           var10001 = false;
                           break label819;
                        }
                     }

                     while(true) {
                        var94 = var10000;

                        try {
                           throw var94;
                        } catch (Throwable var86) {
                           var10000 = var86;
                           var10001 = false;
                           continue;
                        }
                     }
                  }

                  if (var3 != null) {
                     this.entryRemoved(false, var1, var2, var3);
                     return var3;
                  }

                  this.trimToSize(this.maxSize);
                  return var2;
               }
            }
         }

         while(true) {
            var94 = var10000;

            try {
               throw var94;
            } catch (Throwable var85) {
               var10000 = var85;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public final int hitCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.hitCount;
      } finally {
         ;
      }

      return var1;
   }

   public final int maxSize() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.maxSize;
      } finally {
         ;
      }

      return var1;
   }

   public final int missCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.missCount;
      } finally {
         ;
      }

      return var1;
   }

   public final Object put(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         synchronized(this){}

         Object var3;
         label240: {
            Throwable var10000;
            boolean var10001;
            label241: {
               try {
                  ++this.putCount;
                  this.size += this.safeSizeOf(var1, var2);
                  var3 = this.map.put(var1, var2);
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label241;
               }

               if (var3 != null) {
                  try {
                     this.size -= this.safeSizeOf(var1, var3);
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label241;
                  }
               }

               label225:
               try {
                  break label240;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label225;
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

         if (var3 != null) {
            this.entryRemoved(false, var1, var3, var2);
         }

         this.trimToSize(this.maxSize);
         return var3;
      } else {
         throw new NullPointerException("key == null || value == null");
      }
   }

   public final int putCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.putCount;
      } finally {
         ;
      }

      return var1;
   }

   public final Object remove(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("key == null");
      } else {
         synchronized(this){}

         Object var2;
         label229: {
            Throwable var10000;
            boolean var10001;
            label230: {
               try {
                  var2 = this.map.remove(var1);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label230;
               }

               if (var2 != null) {
                  try {
                     this.size -= this.safeSizeOf(var1, var2);
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label230;
                  }
               }

               label215:
               try {
                  break label229;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label215;
               }
            }

            while(true) {
               Throwable var23 = var10000;

               try {
                  throw var23;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (var2 != null) {
            this.entryRemoved(false, var1, var2, (Object)null);
         }

         return var2;
      }
   }

   public void resize(int param1) {
      // $FF: Couldn't be decompiled
   }

   public final int size() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.size;
      } finally {
         ;
      }

      return var1;
   }

   protected int sizeOf(Object var1, Object var2) {
      return 1;
   }

   public final Map snapshot() {
      synchronized(this){}

      LinkedHashMap var1;
      try {
         var1 = new LinkedHashMap(this.map);
      } finally {
         ;
      }

      return var1;
   }

   public final String toString() {
      synchronized(this){}

      Throwable var10000;
      label128: {
         int var1;
         boolean var10001;
         try {
            var1 = this.hitCount + this.missCount;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label128;
         }

         if (var1 != 0) {
            try {
               var1 = 100 * this.hitCount / var1;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label128;
            }
         } else {
            var1 = 0;
         }

         label115:
         try {
            String var15 = String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.maxSize, this.hitCount, this.missCount, var1);
            return var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label115;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public void trimToSize(int var1) {
      while(true) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label463: {
            label465: {
               try {
                  if (this.size >= 0 && (!this.map.isEmpty() || this.size == 0)) {
                     break label465;
                  }
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label463;
               }

               try {
                  StringBuilder var2 = new StringBuilder();
                  var2.append(this.getClass().getName());
                  var2.append(".sizeOf() is reporting inconsistent results!");
                  IllegalStateException var3 = new IllegalStateException(var2.toString());
                  throw var3;
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label463;
               }
            }

            label466: {
               try {
                  if (this.size > var1 && !this.map.isEmpty()) {
                     break label466;
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label463;
               }

               try {
                  return;
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label463;
               }
            }

            Object var47;
            Object var49;
            try {
               Entry var46 = (Entry)this.map.entrySet().iterator().next();
               var49 = var46.getKey();
               var47 = var46.getValue();
               this.map.remove(var49);
               this.size -= this.safeSizeOf(var49, var47);
               ++this.evictionCount;
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break label463;
            }

            this.entryRemoved(true, var49, var47, (Object)null);
            continue;
         }

         while(true) {
            Throwable var48 = var10000;

            try {
               throw var48;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
